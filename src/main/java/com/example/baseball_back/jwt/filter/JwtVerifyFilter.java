package com.example.baseball_back.jwt.filter;


import com.example.baseball_back.jwt.JwtUtil;
import com.example.baseball_back.jwt.exception.CustomExpiredJwtException;
import com.example.baseball_back.jwt.exception.CustomJwtException;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    private static final String[] whitelist = {"/signUp", "/login" , "/"
            , "/index.html","/swagger-ui/**", "/v3/api-docs/**","/swagger-ui/index.html","/swagger-ui.html"
    };

    private static final String ACCESS_ENDPOINT = "/api/v1/jwt/access";

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
            String token = JwtUtil.getTokenFromHeader(authHeader);
            System.out.println(token);

            if (ACCESS_ENDPOINT.equals(requestURI)) {
                try {
                    // 토큰 검증 (
                    Map<String, Object> claims = JwtUtil.getClaimsWithoutValidation(token);
                    request.setAttribute("claims", claims);
                } catch (CustomJwtException e) {

                }
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = JwtUtil.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
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
