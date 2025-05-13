package com.jwtauth.assignment.todos;

import com.jwtauth.assignment.common.JWTUtil;
import com.jwtauth.assignment.model.Todo;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodosTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long userIdx;
    private Long createdTodoId;
    private String token;


    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User("TEST_ID", "TEST_PW", "TEST_NAME", "ROLE_USER");
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").value("회원가입 성공"));

        // JWT 토큰 생성
        String userId = "TEST_ID";
        token = jwtUtil.createJwt(userId,"ROLE_USER",  1000 * 60 * 60L);
        userIdx = userService.getUserIdx(userId); // mock 또는 test userIdx
    }

    @AfterEach
    void testIdDelete() throws NotFoundException {
        userService.userDelete(testUser);
    }


    @DisplayName("JWT 없이 접근 시 401 반환")
    @Test
    void testUnauthorizedAccessWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("TODO 생성")
    @Test
    void testTodoCreate() throws Exception {
        Todo todo = Todo.builder()
                .title("TEST TITLE")
                .description("TEST DESCRIPTION")
                .completed(false)
                .build();

        mockMvc.perform(post("/api/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("업로드 성공"));
    }

    @DisplayName("TODO 조회")
    @Test
    @Transactional
    void testTodoList() throws Exception {

        createTodoAndGetId("TEST TITLE", "TEST DESCRIPTION", false);

        MvcResult result = mockMvc.perform(get("/api/todos")
                                    .header("Authorization", "Bearer " + token))
                                    .andExpect(status().isOk())
                                    .andReturn();

        String content = result.getResponse().getContentAsString();
        JsonNode todoList = objectMapper.readTree(content);

        assertThat(todoList.isArray()).isTrue();
        assertThat(todoList.size()).isGreaterThan(0);

        createdTodoId = todoList.get(0).get("todosIdx").asLong(); // 이후 수정/삭제용
    }

    @DisplayName("TODO 수정")
    @Test
    @Transactional
    void testTodoModifying() throws Exception {

        Long todoId = createTodoAndGetId("TEST TITLE", "TEST DESCRIPTION", false);

        Todo updateTodo = Todo.builder()
                .todosIdx(todoId)
                .title("UPDATE TITLE")
                .description("UPDATE DESCRIPTION")
                .completed(true)
                .build();

        mockMvc.perform(put("/api/todos/" + todoId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTodo)))
                .andExpect(status().isOk())
                .andExpect(content().string("수정 성공"));
    }

    @DisplayName("TODO 삭제")
    @Test
    void testTodoDelete() throws Exception {

        Long todoId = createTodoAndGetId("TEST TITLE", "TEST DESCRIPTION", false);

        mockMvc.perform(delete("/api/todos/" + todoId)
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(content().string("삭제 성공"));
    }

    private Long createTodoAndGetId(String title, String description, boolean completed) throws Exception {
        Todo todo = Todo.builder()
                .title(title)
                .description(description)
                .completed(completed)
                .build();

        mockMvc.perform(post("/api/todos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("업로드 성공"));

        MvcResult result = mockMvc.perform(get("/api/todos")
                                            .header("Authorization", "Bearer " + token))
                                            .andExpect(status().isOk())
                                            .andReturn();

        JsonNode todoList = objectMapper.readTree(result.getResponse().getContentAsString());
        return todoList.get(0).get("todosIdx").asLong();
    }

}
