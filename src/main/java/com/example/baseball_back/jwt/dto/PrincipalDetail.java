package com.example.baseball_back.jwt.dto;

import com.example.baseball_back.domain.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private Users user;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public PrincipalDetail(Users user, Collection<? extends GrantedAuthority> authorities) {
        this(user, authorities, new HashMap<>());
    }

    public PrincipalDetail(Users user, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.user = user;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    // info 에 들어가는 것들이 토큰에 들어가는 데이터
    public Map<String, Object> getMemberInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("socialId", user.getSocialId());
        info.put("role", user.getRoleType().name()); // RoleType을 String으로 변환
        return info;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
      return null;
    }

    @Override
    public String getUsername() {
        return user.getSocialId(); // Users 클래스에 getSocialId() 메서드가 있다고 가정
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return user.getSocialId(); // OAuth2User 인터페이스의 메서드 구현
    }
}
