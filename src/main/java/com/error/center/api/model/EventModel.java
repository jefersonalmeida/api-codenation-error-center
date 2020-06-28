package com.error.center.api.model;

import com.error.center.domain.enums.Level;
import com.error.center.domain.validator.EnumValidator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventModel {

    private UUID id;

    @NotBlank
    @EnumValidator(enumClass = Level.class, ignoreCase = true, message = "Para o nível somente são aceitos os valores: ERROR|WARNING|INFO")
    private String level;

    @NotBlank
    private String description;

    @NotBlank
    private String log;

    @NotBlank
    private String origin;

    @NotBlank
    private Date date;

    private Integer quantity = 1;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("last_modified_by")
    private String lastModifiedBy;

    @JsonProperty("last_modified_at")
    private Date lastModifiedAt;
}
