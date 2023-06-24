package com.example.formscreator.exception;

import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, new Date(),ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex){
        ErrorResponse error = new ErrorResponse(400, new Date(),ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex){
        ErrorResponse error = new ErrorResponse(403, new Date(),ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(403).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex){
        ErrorResponse error = new ErrorResponse(401, new Date(),ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex){
        ErrorResponse error = new ErrorResponse(401, new Date(),ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errMessage = error.getDefaultMessage();
            errorMessage.append( errMessage).append("!>");
        });
        String errMessage = errorMessage.toString();
        ErrorResponse error = new ErrorResponse(400, new Date(),ex.getClass().getSimpleName(), errMessage.substring(0, errMessage.length() - 2));
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        
        ErrorResponse error = new ErrorResponse(400, new Date(),ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        ErrorResponse error = new ErrorResponse(500, new Date(),ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }
}
