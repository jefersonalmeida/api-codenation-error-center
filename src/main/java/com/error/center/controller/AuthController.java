package com.error.center.controller;

import com.error.center.dto.LoginDTO;
import com.error.center.dto.TokenDTO;
import com.error.center.dto.UserDTO;
import com.error.center.entity.User;
import com.error.center.response.Response;
import com.error.center.service.UserDetailsService;
import com.error.center.service.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    private UserDetailsService userDetailsService;

    @PostMapping(path = "register")
    public ResponseEntity<Response<UserDTO>> store(@Valid @RequestBody UserDTO dto, BindingResult result) {

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
    public ResponseEntity<Response<TokenDTO>> gerarTokenJwt(
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

}
