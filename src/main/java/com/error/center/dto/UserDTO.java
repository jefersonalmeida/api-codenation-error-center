package com.error.center.dto;

import com.error.center.entity.User;
import com.error.center.util.BCrypt;
import com.error.center.util.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private UUID id;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull(message = "Informe um grupo de acesso")
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$", message = "Para o grupo somente são aceitos os valores ROLE_ADMIN ou ROLE_USER")
    private String role;

    private LocalDateTime created;

    private LocalDateTime updated;

    public User toEntity() {
        User u = new User();
        u.setId(this.getId());
        u.setEmail(this.getEmail());
        u.setPassword(BCrypt.getHash(this.getPassword()));
        u.setCreated(this.getCreated());
        u.setUpdated(this.getUpdated());
        u.setRole(RoleEnum.valueOf(this.getRole()));
        return u;
    }
}