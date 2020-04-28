package com.error.center.service;

import com.error.center.entity.User;
import com.error.center.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public User save(User object) {
        return repository.save(object);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmailEquals(email);
    }
}
