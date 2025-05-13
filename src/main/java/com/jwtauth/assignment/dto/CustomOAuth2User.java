package com.jwtauth.assignment.dto;

import com.jwtauth.assignment.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final User user;

    public CustomOAuth2User(User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", user.getUserId());
        attributes.put("name", user.getName());
        attributes.put("email", user.getEmail());

        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public String getUserId() {
        return user.getUserId();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
