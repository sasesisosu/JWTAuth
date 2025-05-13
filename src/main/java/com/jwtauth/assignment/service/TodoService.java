package com.jwtauth.assignment.service;

import com.jwtauth.assignment.model.Todo;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.List;

public interface TodoService {

    void uploadTodo(Todo todo, Long userIdx);

    List<Todo> getTodoList(Long userIdx) throws NotFoundException;

    Todo getIdTodoList(Long todosIdx) throws NotFoundException;

    void todoModifying(Long todosIdx, Todo todo);

    void todoDelete(Long todosIdx);

    List<Todo> getTodoSearch(String title) throws NotFoundException;
}
