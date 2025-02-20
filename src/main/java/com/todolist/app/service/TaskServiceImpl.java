package com.todolist.app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.todolist.app.entity.Task;
import com.todolist.app.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
    
    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task Task){
        return taskRepository.save(Task);
    }

    @Override
    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long id) {
        Task task = null;
        try {
            task = taskRepository.findById(id).get();

        } catch (Exception e){
            return null;
        }
        return task;
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findTaskByTitle(String title) {
        return taskRepository.findTaskByTitle(title);
    }

}
