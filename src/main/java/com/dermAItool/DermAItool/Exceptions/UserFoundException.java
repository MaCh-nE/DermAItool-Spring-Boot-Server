package com.dermAItool.DermAItool.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserFoundException extends RuntimeException {
    public UserFoundException(String errorMessage) {
        super(errorMessage);
    }
}
