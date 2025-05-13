package com.jwtauth.assignment.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jwtauth.assignment.model.User;
import com.jwtauth.assignment.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    private User testUser;

    @BeforeEach
    void setup() throws Exception {
        testUser = new User("TEST_ID", "TEST_PW", "TEST_NAME", "ROLE_USER");
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").value("회원가입 성공"));
    }

    @AfterEach
    void testIdDelete() throws NotFoundException {
        userService.userDelete(testUser);
    }

    @DisplayName("JWT 없이 접근 시 401 반환")
    @Test
    void testAccessWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("유효하지 않은 ID 접근 시 404 반환")
    @Test
    void testUserModifyingWithInvalidId() throws Exception {
        String token = createToken();

        // 존재하지 않는 ID를 사용하여 User 객체 생성
        User invalidUser = new User("INVALID_USER_ID", "newPassword", "newName", "ROLE_USER");

        mockMvc.perform(put("/api/users/me")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("회원가입 테스트")
    @Test
    @Transactional
    void testSignup() throws Exception {
        User newUser = new User("TEST_SIGN_ID", "TEST_PW", "TEST_NAME", "ROLE_USER");

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").value("회원가입 성공"));
    }

    @DisplayName("로그인 테스트")
    @Test
    @Transactional
    void testLogin() throws Exception {

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.access_token").exists());
    }

    @DisplayName("JWT 인증 테스트")
    @Test
    @Transactional
    void testAccessWithJwt() throws Exception {

        String token = createToken();

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", token))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.userId").value("TEST_ID"))
                        .andExpect(jsonPath("$.name").value("TEST_NAME"));
    }

    private String createToken() throws Exception {
        String response = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        String token = json.get("access_token").asText();
        return token;
    }
}
