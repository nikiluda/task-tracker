package com.zhanlin.task_tracker.task.domain.exception;

import com.zhanlin.task_tracker.shared.domain.exception.NotFoundException;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException() {
      super("Task not found");
    }
}
