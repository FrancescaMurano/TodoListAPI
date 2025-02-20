package com.todolist.app.service;

import java.util.List;
import com.todolist.app.entity.Task;


public interface TaskService {

    Task save(Task task);
    List<Task> findAll();
    Task findById(Long id);
    void deleteById(Long id);
    List<Task> findTaskByTitle(String title);
}  
