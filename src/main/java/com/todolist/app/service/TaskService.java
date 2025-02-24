package com.todolist.app.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.todolist.app.entity.Task;

public interface TaskService {

    Task save(Task task);
    Page<Task> findAll(Pageable page);
    Task findById(Long id);
    void deleteById(Long id);
    List<Task> findTaskByTitle(String title);
    Page<Task> findTaskByOwners(Pageable pageable, List<String> owner);
    Boolean isOwner(Long id, String username);
}  
