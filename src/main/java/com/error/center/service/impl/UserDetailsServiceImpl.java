package com.error.center.service.impl;

import com.error.center.entity.User;
import com.error.center.service.UserService;
import com.error.center.util.jwt.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userService.findByEmail(email);

        if (user.isPresent()) return JwtUserFactory.create(user.get());

        throw new UsernameNotFoundException("Email não encontrado.");
    }

}