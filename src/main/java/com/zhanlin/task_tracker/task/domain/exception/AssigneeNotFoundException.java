package com.zhanlin.task_tracker.task.domain.exception;

import com.zhanlin.task_tracker.shared.domain.exception.NotFoundException;

public class AssigneeNotFoundException extends NotFoundException {
    public AssigneeNotFoundException() {
        super("Assignee not found");
    }
}
