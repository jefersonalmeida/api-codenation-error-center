package com.error.center.api.model;

import com.error.center.domain.enums.Level;
import com.error.center.domain.validator.EnumValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class EventInput {
    @NotBlank
    @NotNull
    @EnumValidator(enumClass = Level.class, ignoreCase = true, message = "Para o nível somente são aceitos os valores: ERROR|WARNING|INFO")
    private String level;

    @NotBlank
    private String description;

    @NotBlank
    private String log;

    @NotBlank
    private String origin;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    private Date date;
}
