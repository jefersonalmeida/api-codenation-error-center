package com.error.center.util;

import com.error.center.dto.UserDTO;
import com.error.center.entity.User;
import com.error.center.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Util {

    private static UserService staticService;

    public Util(UserService service) {
        Util.staticService = service;
    }

    public static UUID getUserId() {
        try {
            Optional<User> user = staticService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            return user.map(User::getId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static UserDTO getUserDTO() {
        try {
            Optional<User> user = staticService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            return user.map(User::toDTO).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}