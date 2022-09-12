package com.task.githubTask.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseNotCreatedException extends RuntimeException {
    public ResponseNotCreatedException(String message) {
        super(message);
    }
}
