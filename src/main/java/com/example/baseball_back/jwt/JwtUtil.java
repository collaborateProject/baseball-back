package com.example.baseball_back.jwt;

import com.example.baseball_back.domain.RoleType;
import com.example.baseball_back.domain.Users;
import com.example.baseball_back.jwt.dto.PrincipalDetail;
import com.example.baseball_back.jwt.exception.CustomExpiredJwtException;
import com.example.baseball_back.jwt.exception.CustomJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;


@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String secretKey;

    @Value("${jwt.access-exp-time}")
    private int accessExpTime;

    @Value("${jwt.refresh-exp-time}")
    private int refreshExpTime;

    private static int ACCESS_EXP_TIME;
    private static int REFRESH_EXP_TIME;
    private static String SECRET_KEY;


    @PostConstruct
    public void init() {
        ACCESS_EXP_TIME = this.accessExpTime;
        REFRESH_EXP_TIME = this.refreshExpTime;
        SECRET_KEY = this.secretKey;

    }



    // 토큰 추출
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    //newAccessToken 생성
    public static String createNewAccessToken(Map<String,Object>valueMap){
        Map<String, Object> info = new HashMap<>();
        String socialId = (String) valueMap.get("socialId");
        String role = (String) valueMap.get("role");
        info.put("socialId",socialId);
        info.put("role", role);
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + ACCESS_EXP_TIME);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(info)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public static String createNewRefreshToken(Map<String,Object>valueMap){
        Map<String, Object> info = new HashMap<>();
        String socialId = (String) valueMap.get("socialId");
        String role = (String) valueMap.get("role");
        info.put("socialId",socialId);
        info.put("role", role);
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + REFRESH_EXP_TIME);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(info)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }
    // accessToken 생성
    public static String createAccessToken(Map<String, Object> valueMap) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + ACCESS_EXP_TIME);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(valueMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    //refreshToken 생성
    public static String refreshToken(Map<String, Object> valueMap) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + REFRESH_EXP_TIME);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(valueMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // 만료된 토큰에서 클레임을 가져옴
    public static Map<String, Object> getClaimsWithoutValidation(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            log.warn("Expired JWT token. Extracting claims without validation.");
            return expiredJwtException.getClaims();
        }
    }

    public static Authentication getAuthentication(String token) {
        Map<String, Object> claims = validateToken(token);
        System.out.println(claims);
        String socialId = (String) claims.get("socialId");
        String role = (String) claims.get("role");
        RoleType memberRole = RoleType.valueOf(role);

        Users user = Users.builder()
                .socialId(socialId).roleType(memberRole)
                .build();
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRoleType().name()));
        PrincipalDetail principalDetail = new PrincipalDetail(user, authorities);

        return new UsernamePasswordAuthenticationToken(principalDetail, "", authorities);
    }

    // 토큰의 유효성 + 만료일자 확인
    public static boolean isValidateToken(String jwtToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            return false;
        }
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();
        } catch(ExpiredJwtException expiredJwtException){
            throw new CustomExpiredJwtException("토큰이 만료되었습니다", expiredJwtException);
        } catch(Exception e){
            throw new CustomJwtException("유효하지 않은 토큰입니다", e);
        }
        return claim;
    }
    // 토큰의 남은 만료시간
    public static long tokenRemainTime(Integer expTime) {
        Date expDate = new Date((long) expTime * (1000));
        long remainMs = expDate.getTime() - System.currentTimeMillis();
        return remainMs / (1000 * 60);
    }



}
