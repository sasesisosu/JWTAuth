package com.jwtauth.assignment.dao;

import com.jwtauth.assignment.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void signup(User user);

    void naverSignup(User user);

    boolean existsById(String id);

    User findByUserId(String userId);

    void userModifying(User user);

    void userDelete(User user);

    Long getUserIdx(String userId);
}
