package com.resale.homeflyinventory.shared;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.resale.homeflyinventory.utils.MessageUtil;
import com.resale.homeflyinventory.utils.ReturnObject;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final MessageUtil messageUtil;

    public GlobalExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ReturnObject<Void>> handleAllExceptions(Exception ex) {
        ReturnObject<Void> response = new ReturnObject<>();
        System.out.println("HandleAllExceptions : " + ex.getMessage());
        response.setStatus(false);
        response.setMessage("Something went wrong");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ReturnObject<Void>> handlePermissionDenied(PermissionDeniedException ex) {
        ReturnObject<Void> response = new ReturnObject<>();
        System.out.println("HandlePermissionDenied : " + ex.getMessage());
        response.setMessage(ex.getMessage());
        response.setStatus(false);
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ReturnObject<Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        ReturnObject<Object> response = new ReturnObject<>();
        response.setStatus(false);
        response.setMessage("Validation failed");
        response.setData(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<ReturnObject<Void>> handleBadRequest(Exception ex) {

        ReturnObject<Void> response = new ReturnObject<>();
        response.setStatus(false);
        response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ReturnObject<Void>> handleExpiredJwt() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ReturnObject<>(
                        messageUtil.getMessage("location.token.expired"),
                        false,
                        null
                ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ReturnObject<Void>> handleAuthException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ReturnObject<>(
                        messageUtil.getMessage("location.authenticate"),
                        false,
                        null
                ));
    }
}


