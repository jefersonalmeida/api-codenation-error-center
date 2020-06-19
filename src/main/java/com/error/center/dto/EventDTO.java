package com.error.center.dto;

import com.error.center.util.enums.Level;
import com.error.center.validators.enums.EnumValidator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDTO {

    private UUID id;

    @NotNull(message = "O nível é obrigatório")
    @NotEmpty(message = "O nível não pode estar vazio")
    @EnumValidator(enumClass = Level.class, ignoreCase = true, message = "Para o nível somente são aceitos os valores: ERROR|WARNING|INFO")
    private String level;

    @NotNull(message = "A descrição é obrigatória")
    @NotEmpty(message = "A descrição não pode estar vazia")
    private String description;

    @NotNull(message = "O log é obrigatório")
    @NotEmpty(message = "O log não pode estar vazio")
    private String log;

    @NotNull(message = "A origem é obrigatória")
    @NotEmpty(message = "A origem não pode estar vazia")
    private String origin;

    @NotNull(message = "Informe uma data")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private Integer quantity = 1;

    private String createdBy;
    private Date createdAt;
    private String lastModifiedBy;
    private Date lastModifiedAt;
}
