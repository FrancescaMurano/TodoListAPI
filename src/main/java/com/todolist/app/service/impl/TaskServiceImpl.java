package com.todolist.app.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.todolist.app.entity.Task;
import com.todolist.app.repository.TaskRepository;
import com.todolist.app.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task save(Task Task){
        return taskRepository.save(Task);
    }

    @Override
    public Page<Task> findAll(Pageable pageable){
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> findTaskByOwners(Pageable pageable, List<String> owners) {
        return taskRepository.findAllByOwnerIn(pageable, owners);
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

    @Override
    public Boolean isOwner(Long id, String username) {
        if(taskRepository.findTaskByIdAndOwner(id,username) != null){
            return true;
        }
        return false;
    }


}
