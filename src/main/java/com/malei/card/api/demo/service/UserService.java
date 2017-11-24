package com.malei.card.api.demo.service;

import com.malei.card.api.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    User getById(String id);
    User updateUser(User user);
    void deleteUser(User user);
    User saveUser(User user);
}
