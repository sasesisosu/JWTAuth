package com.jwtauth.assignment.dto;

public interface OAuth2Response {

    /**
     * 제공자 - NAVER
     */
    String getProvider();

    /**
     * 발급해주는 아이디(번호)
     */
    String getProviderId();

    String getEmail();

    String getName();
}
