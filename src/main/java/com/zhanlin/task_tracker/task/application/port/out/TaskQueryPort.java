package com.zhanlin.task_tracker.task.application.port.out;

import com.zhanlin.task_tracker.task.application.model.TaskPage;
import com.zhanlin.task_tracker.task.application.model.TaskPageRequest;
import com.zhanlin.task_tracker.task.application.model.TaskSearchCriteria;

public interface TaskQueryPort {

    TaskPage findTasks(
            TaskSearchCriteria criteria,
            TaskPageRequest pageRequest
    );
}
