package com.zhanlin.task_tracker.identity.domain.exception;

import com.zhanlin.task_tracker.shared.domain.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String email) {

        super("User not found: " + email);
    }
}
