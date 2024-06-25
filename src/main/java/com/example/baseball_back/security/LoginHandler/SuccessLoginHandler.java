package com.example.baseball_back.security.LoginHandler;


import com.example.baseball_back.jwt.JwtUtil;
import com.example.baseball_back.jwt.dto.PrincipalDetail;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class SuccessLoginHandler  implements AuthenticationSuccessHandler {


    @Value("${jwt.access-exp-time}")
    private int accessExpTime;

    @Value("${jwt.refresh-exp-time}")
    private int refreshExpTime;

    private static int ACCESS_EXP_TIME;
    private static int REFRESH_EXP_TIME;

    @PostConstruct
    public void init() {
        ACCESS_EXP_TIME = this.accessExpTime;
        REFRESH_EXP_TIME = this.refreshExpTime;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
            , HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {


        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();

        Map<String, Object> responseMap = principal.getMemberInfo();
        responseMap.put("accessToken", JwtUtil.generateToken(responseMap, SuccessLoginHandler.ACCESS_EXP_TIME));
        responseMap.put("refreshToken", JwtUtil.generateToken(responseMap, SuccessLoginHandler.REFRESH_EXP_TIME));

        System.out.println(responseMap);


        Gson gson = new Gson();
        String json = gson.toJson(responseMap);

        response.setContentType("application/json; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
    }

}
