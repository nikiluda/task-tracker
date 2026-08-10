package com.zhanlin.task_tracker.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String email) {

        super("User not found: " + email);
    }
}
