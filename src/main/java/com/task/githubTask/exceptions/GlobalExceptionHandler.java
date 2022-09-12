package com.task.githubTask.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", exception.getRawStatusCode());
        body.put("Message", exception.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<Object> handleResponseNotCreatedException(Exception exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Message", "Response can't be casted to the object\n" + exception.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseNotCreatedException.class)
    public ResponseEntity<Object> handleRestClientException() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Message", "Response can't be created");
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
