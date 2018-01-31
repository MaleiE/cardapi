package com.malei.card.api.demo.service.impl;

import com.malei.card.api.demo.dto.PaymentsDto;
import com.malei.card.api.demo.exception.EntityNotFoundException;
import com.malei.card.api.demo.exception.UserNotFoundException;
import com.malei.card.api.demo.model.User;
import com.malei.card.api.demo.repository.PurchaseRepository;
import com.malei.card.api.demo.repository.UserRepository;
import com.malei.card.api.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

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

    @Override
    public List<PaymentsDto> getUserPayments(String userId) {
        return null;
    }

}
