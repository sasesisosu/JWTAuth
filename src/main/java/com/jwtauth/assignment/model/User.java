package com.jwtauth.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class User {
    /**
     * 사용자 일련번호
     */
    private Long userIdx;

    /**
     * 사용자 아이디
     */
    private String userId;

    /**
     * 사용자 비밀번호
     */
    private String password;

    /**
     * 사용자 이름
     */
    private String name;

    private String role;

    public User(String userId, String password, String name, String role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
