package com.jwtauth.assignment.service.Impl;


import com.jwtauth.assignment.dao.UserMapper;
import com.jwtauth.assignment.dto.CustomOAuth2User;
import com.jwtauth.assignment.dto.NaverResponse;
import com.jwtauth.assignment.dto.OAuth2Response;
import com.jwtauth.assignment.model.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * NAVER 로그인 요청 처리
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;

    public CustomOAuth2UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String userId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        User existUser = userMapper.findByUserId(userId);

        User user = new User();
        user.setUserId(userId);
        user.setEmail(oAuth2Response.getEmail());
        user.setName(oAuth2Response.getName());
        user.setRole("ROLE_USER");
        if(existUser == null) {
            userMapper.naverSignup(user);
        }
        return new CustomOAuth2User(user);
    }
}
