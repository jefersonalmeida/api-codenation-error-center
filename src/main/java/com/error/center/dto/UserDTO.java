package com.error.center.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private UUID id;

    @NotNull(message = "O nome é obrigatório")
    @NotEmpty(message = "O nome é obrigatório")
    private String name;

    @NotNull(message = "O email é obrigatório")
    @NotEmpty(message = "O email é obrigatório")
    @Email(message = "O email precisa ser válido")
    private String email;

    @NotEmpty(message = "O email é obrigatório")
    @NotNull(message = "O email é obrigatório")
    @Size(min = 6, max = 50, message = "A senha precisa ter ao menos 6 caracteres e no máximo 50")
    private String password;

    private String role;
    private Date created_at;
    private Date updated_at;
}
