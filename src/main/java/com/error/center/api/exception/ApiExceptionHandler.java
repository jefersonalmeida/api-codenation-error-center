package com.error.center.api.exception;

import com.error.center.api.response.ResponseError;
import com.error.center.domain.exception.ApiInvalidArgumentException;
import com.error.center.domain.exception.ApiModelExistsException;
import com.error.center.domain.exception.ApiModelNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleUnauthorized(AuthenticationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseException responseException = new ResponseException(
                status.value(),
                "Suas credenciais são inválidas."
        );
        ResponseError<ResponseException> response = new ResponseError<>();
        response.setError(responseException);
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ApiModelNotFoundException.class)
    public ResponseEntity<Object> handleModelExists(ApiModelNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ResponseException responseException = new ResponseException(status.value(), ex.getMessage());

        ResponseError<ResponseException> response = new ResponseError<>();
        response.setError(responseException);
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ApiModelExistsException.class)
    public ResponseEntity<Object> handleModelExists(ApiModelExistsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ResponseException responseException = new ResponseException(status.value(), ex.getMessage());

        ResponseError<ResponseException> response = new ResponseError<>();
        response.setError(responseException);
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ApiInvalidArgumentException.class)
    public ResponseEntity<Object> handleInvalidArgument(ApiInvalidArgumentException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseException responseException = new ResponseException(status.value(), ex.getMessage());

        ResponseError<ResponseException> response = new ResponseError<>();
        response.setError(responseException);
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<ResponseException.Field> fields = new ArrayList<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(e -> {
                    String name = ((FieldError) e).getField();
                    String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
                    fields.add(new ResponseException.Field(name, message));
                });

        ResponseException responseException = new ResponseException(
                status.value(),
                "Encontramos erros na validação dos dados.",
                fields
        );
        ResponseError<ResponseException> response = new ResponseError<>();
        response.setError(responseException);
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }
}
