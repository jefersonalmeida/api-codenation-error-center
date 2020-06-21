package com.error.center.controller;

import com.error.center.dto.LoginDTO;
import com.error.center.dto.TokenDTO;
import com.error.center.dto.UserDTO;
import com.error.center.entity.User;
import com.error.center.mapper.UserMapper;
import com.error.center.response.Response;
import com.error.center.service.UserService;
import com.error.center.service.impl.UserDetailsServiceImpl;
import com.error.center.util.enums.Role;
import com.error.center.util.jwt.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@AllArgsConstructor
@RequestMapping(path = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Auth", description = "Recurso para gestão de usuários.")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtToken jwtToken;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Operation(summary = "Cadastro de usuário", description = "Cadastra um usuário.")
    @PostMapping(path = "register")
    public ResponseEntity<Response<UserDTO>> store(@Valid @RequestBody UserDTO dto, BindingResult result) {
        dto.setRole(Role.ROLE_USER.toString());
        Optional<User> exists = this.userService.findByEmail(dto.getEmail());
        if (exists.isPresent()) {
            result.addError(new ObjectError("User.email", dto.getEmail() + " já está cadastrado"));
        }

        Response<UserDTO> response = new Response<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }


        User user = userService.save(userMapper.toEntity(dto));
        response.setData(userMapper.toDTO(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Autenticação", description = "Autentica o usuário.")
    @PostMapping(path = "login")
    public ResponseEntity<Response<TokenDTO>> login(@Valid @RequestBody LoginDTO dto, BindingResult result) throws AuthenticationException {
        Response<TokenDTO> response = new Response<>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getEmail(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtToken.getToken(userDetails);

        Optional<User> user = userService.findByEmail(dto.getEmail());

        response.setData(new TokenDTO(token, userMapper.toDTO(user.orElse(null))));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Perfil", description = "Retorna os dados do usuário autenticado.")
    @GetMapping(path = "whoami")
    public ResponseEntity<Response<UserDTO>> whoami() {
        Response<UserDTO> response = new Response<>();
        Optional<User> user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        response.setData(userMapper.toDTO(user.orElse(null)));
        System.out.println(userMapper.toDTO(user.orElse(null)));
        return ResponseEntity.ok().body(response);
    }
}
