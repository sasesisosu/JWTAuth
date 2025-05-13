package com.jwtauth.assignment.dao;

import com.jwtauth.assignment.model.Todo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodoMapper {
    void uploadTodo(Todo todo);

    List<Todo> getTodoList(Long userIdx);

    Todo getIdTodoList(Long todosIdx);

    int todoModifying(Todo todo);

    int todoDelete(Long todosIdx);

    List<Todo> getTodoSearch(String title);
}
