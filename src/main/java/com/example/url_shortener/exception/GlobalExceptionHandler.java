package com.example.url_shortener.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Object> buildResponse(HttpStatus status, Object message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(UrlNotFoundException ex) {
        LOG.warn("URL not found : {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<Object> handleExpired(UrlExpiredException ex) {
        LOG.warn("URL Expired : {}", ex.getMessage());
        return buildResponse(HttpStatus.GONE, ex.getMessage());
    }

//    @ExceptionHandler(InvalidUrlException.class)
//    public ResponseEntity<Object> handleInvalidUrl(InvalidUrlException ex) {
//    LOG.warn("Invalid full URL : {}", ex.getMessage());
//        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
               errors.put(error.getField(), error.getDefaultMessage())
        );
        LOG.warn("URL not valid : {}", errors);
        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex) {
        LOG.warn("Malformed JSON request : {}", "Invalid request format or data types");
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request format or data types");
    }

    // Generic fallback for any other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        LOG.warn("An exception occurred : {}", ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }
}
