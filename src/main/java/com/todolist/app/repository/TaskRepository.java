package com.todolist.app.repository;
import com.todolist.app.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTaskByTitle(String title); // modello e primary key

    
} 
