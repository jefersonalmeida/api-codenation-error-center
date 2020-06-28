package com.error.center.domain.service;

import com.error.center.core.jwt.JwtUserFactory;
import com.error.center.domain.enums.Role;
import com.error.center.domain.exception.ApiModelExistsException;
import com.error.center.domain.model.User;
import com.error.center.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User save(User object) {
        return userRepository.save(object);
    }

    public User register(User object) {
        System.out.println(object);
        Optional<User> exists = userRepository.findByEmailEquals(object.getEmail());
        if (exists.isPresent() && !exists.get().equals(object)) {
            throw new ApiModelExistsException("Email já cadastrado.");
        }
        object.setRole(Role.ROLE_USER);
        return userRepository.save(object);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailEquals(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmailEquals(email);
        if (user.isPresent()) return JwtUserFactory.create(user.get());
        throw new UsernameNotFoundException("Email não encontrado.");
    }
}
