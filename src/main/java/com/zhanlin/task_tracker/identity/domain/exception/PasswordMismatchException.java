package com.zhanlin.task_tracker.identity.domain.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {

        super("Password do not match");
    }
}
