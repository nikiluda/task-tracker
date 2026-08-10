package com.zhanlin.task_tracker.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String email) {

        super("Email already in use: " + email);
    }
}
