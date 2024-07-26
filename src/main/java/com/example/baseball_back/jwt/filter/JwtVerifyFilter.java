package com.example.baseball_back.jwt.filter;


import com.example.baseball_back.jwt.JwtUtil;
import com.example.baseball_back.jwt.exception.CustomExpiredJwtException;
import com.example.baseball_back.jwt.exception.CustomJwtException;
import com.example.baseball_back.service.RedisUtils;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtVerifyFilter extends OncePerRequestFilter {

    private static final String[] whitelist = {"/signUp", "/login" , "/"
            , "/index.html","/swagger-ui/**", "/v3/api-docs/**","/swagger-ui/index.html","/swagger-ui.html"
    };

    private static final String ACCESS_ENDPOINT = "/api/v1/jwt/access";
    private final RedisUtils redisUtils;

    private static void checkAuthorizationHeader(String header) {
        if(header == null) {
            throw new CustomJwtException("토큰이 전달되지 않았습니다");
        } else if (!header.startsWith("Bearer")) {
            throw new CustomJwtException("Bearer 로 시작하지 않습니다.");
        }
    }

    // whiteList
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        try {
            // header 가 올바른 형식인지 체크
            checkAuthorizationHeader(authHeader);
            String accessToken = JwtUtil.getTokenFromHeader(authHeader);

            if(accessToken != null){
                System.out.println(accessToken);
                if(!JwtUtil.isValidateToken(accessToken)){
                    //만료된 토큰에서 refreshtoken 가져오기
                    Map<String,Object> claim = JwtUtil.getClaimsWithoutValidation(accessToken);
                    String socialId = (String) claim.get("socialId");
                    String refreshToken = redisUtils.getData(socialId);
                    System.out.println(refreshToken);
                    //리프레시 토큰 검증
                    boolean validateRefreshToken = JwtUtil.isValidateToken(refreshToken);
                    if(validateRefreshToken){
                        Map<String,Object>refreshClaim = JwtUtil.validateToken(refreshToken);
                        accessToken = JwtUtil.createNewAccessToken(refreshClaim);

                        //만료시간이 한시간도 남지 않으면 리프레쉬 토큰 재발급
                        long expTime = JwtUtil.tokenRemainTime((Integer) refreshClaim.get("exp"));
                        if (expTime <= 60) {
                            String newRefreshToken = JwtUtil.createNewRefreshToken(refreshClaim);
                            redisUtils.setData(socialId, newRefreshToken);
                        }
                    }

                }

            }

            Authentication authentication = JwtUtil.getAuthentication(accessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            Gson gson = new Gson();
            String json = "";
            if (e instanceof CustomExpiredJwtException) {
                json = gson.toJson(Map.of("토큰 문제", e.getMessage()));
            } else {
                json = gson.toJson(Map.of("error", e.getMessage()));
                System.out.println(json);
            }

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(json);
            printWriter.close();
        }
    }
}
