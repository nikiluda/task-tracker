package com.zhanlin.task_tracker.exception;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException(String message) {
      super(message);
    }
}
