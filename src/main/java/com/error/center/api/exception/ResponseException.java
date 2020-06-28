package com.error.center.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseException {

    private Integer status;
    private OffsetDateTime date;
    private String message;
    private List<Field> fields;

    public ResponseException() {
        this.setDate(OffsetDateTime.now());
    }

    public ResponseException(Integer status) {
        this.setStatus(status);
        this.setDate(OffsetDateTime.now());
    }

    public ResponseException(Integer status, String message) {
        this.setStatus(status);
        this.setMessage(message);
        this.setDate(OffsetDateTime.now());
    }

    public ResponseException(Integer status, String message, List<Field> fields) {
        this.setStatus(status);
        this.setMessage(message);
        this.setFields(fields);
        this.setDate(OffsetDateTime.now());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Field {
        private String name;
        private String message;
    }
}
