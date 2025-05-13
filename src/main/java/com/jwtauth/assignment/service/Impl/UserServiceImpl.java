package com.jwtauth.assignment.service.Impl;

import com.jwtauth.assignment.common.JWTUtil;
import com.jwtauth.assignment.dao.UserMapper;
import com.jwtauth.assignment.model.User;
import com.jwtauth.assignment.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JWTUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 1시간


    @Value("${jwt.secretKey}")
    private static String secretKey;

//    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    @Override
    public void signup(User user) {
        if(userMapper.existsById(user.getUserId())){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userMapper.signup(user);
    }

    @Override
    public String accessToken(User user) {
        return jwtUtil.createJwt(user.getUserId(), user.getRole(), ACCESS_TOKEN_VALIDITY);
    }

    @Override
    public void login(User user, String jwtToken, HttpServletResponse httpResponse) throws NotFoundException {
        httpResponse.addHeader("Authorization", "Bearer " + jwtToken);

        User userInfo = userMapper.findByUserId(user.getUserId());
        if(userInfo == null){
            throw new NotFoundException("존재하지 않는 아이디입니다.");
        }

        if(!passwordEncoder.matches(user.getPassword(), userInfo.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public User getUserInfo(String userId) throws NotFoundException {
        User userInfo = userMapper.findByUserId(userId);
        if(userInfo == null){
            throw new NotFoundException("회원정보가 없습니다.");
        }
        return userInfo;
    }

    @Override
    public void userModifying(User user) throws NotFoundException {
        User userInfo = userMapper.findByUserId(user.getUserId());
        if(userInfo == null){
            throw new NotFoundException("회원정보가 없습니다.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.userModifying(user);
        log.info("user: {}", user);
    }

    @Override
    public void userDelete(User user) throws NotFoundException {
        User userInfo = userMapper.findByUserId(user.getUserId());
        if(userInfo == null){
            throw new NotFoundException("회원정보가 없습니다.");
        }
        userMapper.userDelete(user);
        log.info("user: {}", user);
    }

    @Override
    public Long getUserIdx(String userId) {
        return userMapper.getUserIdx(userId);
    }
}
