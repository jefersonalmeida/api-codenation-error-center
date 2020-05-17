package com.error.center.controller;

import com.error.center.dto.LoginDTO;
import com.error.center.dto.TokenDTO;
import com.error.center.dto.UserDTO;
import com.error.center.entity.User;
import com.error.center.response.Response;
import com.error.center.service.impl.UserDetailsServiceImpl;
import com.error.center.service.UserService;
import com.error.center.util.Util;
import com.error.center.util.jwt.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "auth")
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping(path = "register")
    public ResponseEntity<Response<UserDTO>> store(@Valid @RequestBody UserDTO dto, BindingResult result) {

        Optional<User> exists = this.service.findByEmail(dto.getEmail());
        if (exists.isPresent()) {
            result.addError(new ObjectError("User.email", dto.getEmail() + " já está cadastrado"));
        }

        Response<UserDTO> response = new Response<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        User user = service.save(dto.toEntity());
        response.setData(user.toDTO());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "login")
    public ResponseEntity<Response<TokenDTO>> login(
            @Valid @RequestBody LoginDTO loginDto, BindingResult result)
            throws AuthenticationException {
        Response<TokenDTO> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        String token = jwtToken.getToken(userDetails);
        response.setData(new TokenDTO(token));

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "profile")
    public ResponseEntity<Response<UserDTO>> profile() {
        Response<UserDTO> response = new Response<>();

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = service.findByEmail(authentication.getName());
        user.ifPresent(value -> response.setData(value.toDTO()));*/


        response.setData(Util.getUserDTO());

        return ResponseEntity.ok().body(response);
    }
}
