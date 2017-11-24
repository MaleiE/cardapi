package com.malei.card.api.demo.service.impl;

import com.malei.card.api.demo.exception.UserNotFoundException;
import com.malei.card.api.demo.model.User;
import com.malei.card.api.demo.repository.UserRepository;
import com.malei.card.api.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getById(String id) {
            return userRepository.findById(Long.parseLong(id))
                    .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
