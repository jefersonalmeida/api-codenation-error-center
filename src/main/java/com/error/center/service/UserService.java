package com.error.center.service;

import com.error.center.entity.User;

import java.util.Optional;

public interface UserService {

    User save(User object);

    Optional<User> findByEmail(String email);
}