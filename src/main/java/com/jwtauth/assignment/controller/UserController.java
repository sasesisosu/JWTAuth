package com.jwtauth.assignment.controller;

import com.jwtauth.assignment.common.JWTUtil;
import com.jwtauth.assignment.model.User;
import com.jwtauth.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {

        try {
            userService.signup(user);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletResponse httpResponse)  {

        try {
            String jwtToken = userService.accessToken(user);
            userService.login(user, jwtToken, httpResponse);

            Map<String, String> response = new HashMap<>();
            response.put("access_token", "Bearer " + jwtToken);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("message" ,exception.getMessage()));
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getUserInfo(HttpServletRequest httpRequest) {
        try {
            String authorization = httpRequest.getHeader("Authorization");
            String token = authorization.split(" ")[1];
            String userId = jwtUtil.getUserId(token);

            User userInfo = userService.getUserInfo(userId);

            Map<String, String> response = new HashMap<>();
            response.put("userId", userId);
            response.put("name", userInfo.getName());

            return ResponseEntity.ok(response);
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message" ,exception.getMessage()));
        }
    }

    @PutMapping("/me")
    public ResponseEntity<String> userModifying(@RequestBody User user) {
        try {
            userService.userModifying(user);

            return ResponseEntity.ok("회원정보 수정 성공");
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> userDelete(@RequestBody User user) {
        try {
            userService.userDelete(user);

            return ResponseEntity.ok("회원 삭제 성공");
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
