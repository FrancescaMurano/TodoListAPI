package com.todolist.app.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.app.dto.PaginatedResponse;
import com.todolist.app.entity.Task;
import com.todolist.app.error.ErrorResponse;
import com.todolist.app.service.TaskService;

@RestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/todos")
    public ResponseEntity<Object> postCreateTask(@RequestBody Task task) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> tasks = taskService.findTaskByTitle(task.getTitle());
        
        if(!tasks.isEmpty()){
            ErrorResponse error = new ErrorResponse(400, "Title already exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(error);
        }

        task.setOwner(userDetails.getUsername());
        Task temp = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                         .body(temp);
    }

    @GetMapping("/todos")
    public ResponseEntity<PaginatedResponse<Task>> getAllTasks(  
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(defaultValue = "true") boolean ascending) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> tasks =  taskService.findAll(pageable);
        return ResponseEntity.ok(new PaginatedResponse<>(tasks));
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Object> updateTaskById(@PathVariable Long id, @RequestBody Task newTask) {
        Task task = taskService.findById(id);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "Task not found"));
        }

        else if(taskService.isOwner(id, userDetails.getUsername())){
            newTask.setId(id);
            Task tmp = taskService.save(task);
            return ResponseEntity.status(HttpStatus.OK)
            .body(tmp);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(403, "Permission denied!"));
        }
    }
   
    
    @GetMapping("/todos/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Index must be positive!"));
        }
    
        Task task = taskService.findById(id);

        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Task not found!"));
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Object> deleteTaskById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Index must be positive!"));
        } 
    
        taskService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
