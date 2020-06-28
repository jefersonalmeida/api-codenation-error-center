package com.error.center.api.controller;

import com.error.center.api.model.LoginInput;
import com.error.center.api.model.RegisterInput;
import com.error.center.api.model.TokenModel;
import com.error.center.api.model.UserModel;
import com.error.center.api.response.ResponseResult;
import com.error.center.core.jwt.JwtToken;
import com.error.center.domain.model.User;
import com.error.center.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final ModelMapper modelMapper;

    @Operation(summary = "Cadastro de usuário", description = "Cadastra um usuário.")
    @PostMapping(path = "register")
    public ResponseEntity<ResponseResult<UserModel>> store(@Valid @RequestBody RegisterInput registerInput) {
        ResponseResult<UserModel> responseResult = new ResponseResult<>();

        User user = userService.register(toEntity(registerInput));
        responseResult.setData(toModel(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseResult);
    }

    @Operation(summary = "Autenticação", description = "Autentica o usuário.")
    @PostMapping(path = "login")
    public ResponseEntity<ResponseResult<TokenModel>> login(
            @Valid
            @RequestBody LoginInput input) throws AuthenticationException {

        ResponseResult<TokenModel> responseResult = new ResponseResult<>();

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.getEmail(), input.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userService.loadUserByUsername(input.getEmail());
        String token = jwtToken.getToken(userDetails);

        Optional<User> user = userService.findByEmail(input.getEmail());
        responseResult.setData(new TokenModel(token, toModel(user.orElse(null))));
        return ResponseEntity.ok(responseResult);
    }

    @Operation(summary = "Perfil", description = "Retorna os dados do usuário autenticado.")
    @GetMapping(path = "whoami")
    public ResponseEntity<ResponseResult<UserModel>> whoami() {
        ResponseResult<UserModel> responseResult = new ResponseResult<>();
        Optional<User> user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        responseResult.setData(toModel(user.orElse(null)));
        return ResponseEntity.ok().body(responseResult);
    }

    private UserModel toModel(User model) {
        return modelMapper.map(model, UserModel.class);
    }

    private User toEntity(RegisterInput input) {
        return modelMapper.map(input, User.class);
    }
}
