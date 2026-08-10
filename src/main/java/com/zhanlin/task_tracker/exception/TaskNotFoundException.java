package com.zhanlin.task_tracker.exception;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException() {
      super("Task not found");
    }
}
