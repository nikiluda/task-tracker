package com.zhanlin.task_tracker.exception;

public class AssigneeNotFoundException extends NotFoundException {
    public AssigneeNotFoundException() {
        super("Assignee not found");
    }
}
