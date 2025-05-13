package com.jwtauth.assignment.service;

import com.jwtauth.assignment.model.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.javassist.NotFoundException;

public interface UserService {
    void signup(User user);

    String accessToken(User user);

    void login(User user, String jwtToken, HttpServletResponse httpResponse) throws NotFoundException;

    User getUserInfo(String userId) throws NotFoundException;

    void userModifying(User user) throws NotFoundException;

    void userDelete(User user) throws NotFoundException;

    Long getUserIdx(String userId);

}
