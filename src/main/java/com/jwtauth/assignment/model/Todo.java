package com.jwtauth.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    /**
     * 일련번호
     */
    private Long todosIdx;

    /**
     * 사용자 일련번호
     */
    private Long userIdx;


    /**
     * 사용자 아이디
     */
    private String userId;

    /**
     * 사용자 이메일
     */
    private String email;

    /**
     * TODO 제목
     */
    private String title;

    /**
     * TODO 내용
     */
    private String description;

    /**
     * TODO 완료 여부
     */
    private Boolean completed;

    public Todo(Long userIdx, String title, String description, Boolean completed) {
        this.userIdx = userIdx;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }
}
