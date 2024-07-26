package com.example.baseball_back.security.LoginHandler;
import com.example.baseball_back.jwt.JwtUtil;
import com.example.baseball_back.jwt.dto.PrincipalDetail;
import com.example.baseball_back.service.RedisUtils;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessLoginHandler  implements AuthenticationSuccessHandler {

    private final RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
            , HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {


        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();

        Map<String, Object> responseMap = principal.getMemberInfo();
        String accessToken = JwtUtil.createAccessToken(responseMap);
        String refreshToken = JwtUtil.refreshToken(responseMap);


        //redis에 refreshToken 저장
        redisUtils.setData(principal.getUsername(), refreshToken);

        //client에 accessToken 만 전송
        Map<String,Object> clientResponseMap = new HashMap<>();
        clientResponseMap.put("accessToken", accessToken);

        System.out.println(responseMap);

        Gson gson = new Gson();
        String json = gson.toJson(clientResponseMap);

        response.setContentType("application/json; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
    }

}
