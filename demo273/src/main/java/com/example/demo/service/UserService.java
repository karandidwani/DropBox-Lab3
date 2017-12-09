package com.example.demo.service;


import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> login(String email, String password) {
        System.out.println("userRepository.findByEmailAndPassword(email, password)" +userRepository.findByEmailAndPassword(email, password));
        return userRepository.findByEmailAndPassword(email, password);
    }
}