package com.todolist.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.todolist.app.entity.User;
import com.todolist.app.repository.UserRepository;
import com.todolist.app.service.JWTService;
import com.todolist.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User register(User user) {
        if (repo.findByUsername(user.getUsername()).isPresent()){
            return null;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public List<User> findAll() {
        return repo.findAll();
    }

    @Override
    public User findById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> user = repo.findByUsername(username);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

    @Override
    public Map<String,String> verify(User user) {
        Map<String,String> map = new HashMap<>();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            map.put("token",jwtService.generateToken(user.getUsername(), user.getRoles()));
            map.put("refresh_token", jwtService.generateRefreshToken(user.getUsername()));
        } 
        return map;
    }
    
}
