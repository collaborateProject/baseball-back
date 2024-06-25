package com.example.baseball_back.oauth2;


import com.example.baseball_back.domain.RoleType;
import com.example.baseball_back.domain.Users;
import com.example.baseball_back.jwt.dto.PrincipalDetail;
import com.example.baseball_back.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialId = String.valueOf(attributes.get("id"));
        return processSocialUser(socialId);

    }

    private OAuth2User processSocialUser(String socialId) {
        Optional<Users> bySocialId = usersRepository.findBySocialId(socialId);
        Users user = bySocialId.orElseGet(() -> saveSocialMember(socialId));
        return new PrincipalDetail(user, Collections.singleton(new SimpleGrantedAuthority(user.getRoleType().name())));
    }

    public Users saveSocialMember(String socialId) {

        Users newMember = Users.builder()
                .socialId(socialId)
                .roleType(RoleType.USER)
                .createAt(LocalDateTime.now())
                .build();
        return usersRepository.save(newMember);
    }

}