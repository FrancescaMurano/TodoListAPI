package com.todolist.app.controller;

import org.springframework.web.bind.annotation.RestController;
import com.todolist.app.dto.RegisterRequest;
import com.todolist.app.entity.User;
import com.todolist.app.error.ErrorResponse;
import com.todolist.app.service.JWTService;
import com.todolist.app.service.UserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    @Autowired
    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // @GetMapping("/csrf-token")
    // public CsrfToken getCsrfToken(HttpServletRequest request) {
    //     return (CsrfToken) request.getAttribute("_csrf");
    // }

    @PostMapping("/register")
    public ResponseEntity<Object> postCreateUser(@Valid @RequestBody RegisterRequest request, BindingResult result) {

        if(result.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, result.toString()));
        }

        User userTofind = userService.findUserByUsername(request.getUsername());
        
        if(userTofind != null){
            ErrorResponse error = new ErrorResponse(400, "Username already exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(error);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRoles(request.getRoles());
        userService.register(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                         .body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login (@RequestBody User user){
        return ResponseEntity.status(200).body(userService.verify(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@RequestBody Map<String, String> refreshToken) {

        try {
            String newAccessToken = jwtService.refreshToken(refreshToken);
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(403).body("Refresh token non valido o scaduto!");
        }
    }

}
