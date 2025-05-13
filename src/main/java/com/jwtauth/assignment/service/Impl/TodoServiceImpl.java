package com.jwtauth.assignment.service.Impl;

import com.jwtauth.assignment.dao.TodoMapper;
import com.jwtauth.assignment.model.Todo;
import com.jwtauth.assignment.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    @Autowired
    private final TodoMapper todoMapper;

    @Override
    public void uploadTodo(Todo todo, Long userIdx) {
        todo.setUserIdx(userIdx);
        todoMapper.uploadTodo(todo);
    }

    @Override
    public List<Todo> getTodoList(Long userIdx) throws NotFoundException {
        List<Todo> todoList = todoMapper.getTodoList(userIdx);
        if(todoList.isEmpty()) {
            throw new NotFoundException("항목이 없습니다.");
        }
        return todoList;
    }

    @Override
    public Todo getIdTodoList(Long todosIdx) throws NotFoundException {
        Todo getIdTodoList = todoMapper.getIdTodoList(todosIdx);
        if(getIdTodoList == null) {
            throw new NotFoundException("항목이 없습니다.");
        }
        return getIdTodoList;
    }

    @Override
    public void todoModifying(Long todosIdx, Todo todo)  {
        todo.setTodosIdx(todosIdx);
        int result = todoMapper.todoModifying(todo);
        if(result == 0){
            throw new IllegalArgumentException("수정할 항목이 없습니다.");
        }
    }

    @Override
    public void todoDelete(Long todosIdx)  {
        int result = todoMapper.todoDelete(todosIdx);
        if(result == 0) {
            throw new IllegalArgumentException("삭제할 항목이 없습니다.");
        }
    }

    @Override
    public List<Todo> getTodoSearch(String title) throws NotFoundException {
        List<Todo> todoSearch = todoMapper.getTodoSearch(title);
        if(todoSearch.isEmpty()) {
            throw new NotFoundException("항목이 없습니다.");
        }
        return todoSearch;
    }
}
