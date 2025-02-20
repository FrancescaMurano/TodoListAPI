package com.todolist.app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.todolist.app.entity.User;
import com.todolist.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user){
        String encryptedPass = passwordEncoder.encode(user.getPassword());
        
        User tmp = null;

        try {
            user.setPassword(encryptedPass);
            tmp = userRepository.save(user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return tmp;
    }

    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        User user = null;
        try {
            user = userRepository.findById(id).get();

        } catch (Exception e){
            return null;
        }
        return user;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    

}
