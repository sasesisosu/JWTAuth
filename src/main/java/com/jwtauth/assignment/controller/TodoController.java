package com.jwtauth.assignment.controller;

import com.jwtauth.assignment.common.JWTUtil;
import com.jwtauth.assignment.model.Todo;
import com.jwtauth.assignment.service.TodoService;
import com.jwtauth.assignment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private final TodoService todoService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final JWTUtil jwtUtil;

    /**
     * TODO 업로드
     */
    @PostMapping("")
    public ResponseEntity<String> UploadTodo(@RequestBody Todo todo, HttpServletRequest httpRequest) {
        try {

            String authorization = httpRequest.getHeader("Authorization");
            String token = authorization.split(" ")[1];

            String userId = jwtUtil.getUserId(token);
            Long userIdx = userService.getUserIdx(userId);

            todoService.uploadTodo(todo, userIdx);
            return ResponseEntity.ok("업로드 성공");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * TODO 조회
     */
    @GetMapping("")
    public ResponseEntity<?> getTodoList(HttpServletRequest httpRequest) {
        try {
            String authorization = httpRequest.getHeader("Authorization");

            String token = authorization.split(" ")[1];
            String userId = jwtUtil.getUserId(token);
            Long userIdx = userService.getUserIdx(userId);

            List<Todo> todoList =  todoService.getTodoList(userIdx);

            return ResponseEntity.ok(todoList);

        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    /**
     * TODO ID 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getIdTodoList(@PathVariable("id") Long id) {
        try {
            Todo todoList =  todoService.getIdTodoList(id);

            Map<String, String> response = new HashMap<>();
            response.put("userId", todoList.getUserId());
            response.put("email", todoList.getEmail());
            response.put("title", todoList.getTitle());
            response.put("description", todoList.getDescription());
            response.put("completed", String.valueOf(todoList.getCompleted()));
            return ResponseEntity.ok(response);
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message" ,exception.getMessage()));
        }
    }

    /**
     * TODO 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> todoModifying(@PathVariable("id") Long id, @RequestBody Todo todo) {
        try {
            todoService.todoModifying(id, todo);
            return ResponseEntity.ok("수정 성공");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * TODO 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> todoDelete(@PathVariable("id") Long id) {
        try {
            todoService.todoDelete(id);

            return ResponseEntity.ok("삭제 성공");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * TODO 검색
     */
    @GetMapping("/search")
    public ResponseEntity<?> getTodoSearch(@RequestParam String title) {
        try {
            List<Todo> todoList =  todoService.getTodoSearch(title);
            return ResponseEntity.ok(todoList);
        } catch (NotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
