package com.error.center.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventSampleModel {

    private UUID id;
    private String level;
    private String description;
    private String origin;
    private Date date;
    private Integer quantity;
}
