package com.todolist.app.service;

import java.util.List;
import java.util.Map;

import com.todolist.app.entity.User;


public interface UserService {

    User register(User user);
    List<User> findAll();
    User findById(Long id);
    void deleteById(Long id);
    User findUserByUsername(String username);
    Map<String,String> verify(User user);

    
}  
