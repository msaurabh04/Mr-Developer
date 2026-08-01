package com.example.fullstackapp.service;

import com.example.fullstackapp.dto.UserRegistrationDto;
import com.example.fullstackapp.entity.User;

import java.util.List;

public interface UserService {
    User registerNewUser(UserRegistrationDto dto);
    List<User> findAll();
    User findById(Long id);
    void toggleEnabled(Long id);
    void deleteById(Long id);
}
