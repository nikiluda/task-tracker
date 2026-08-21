package com.zhanlin.task_tracker.task.application.port.out;

import com.zhanlin.task_tracker.task.domain.Task;

import java.util.Optional;

public interface TaskRepositoryPort {

    Task save(Task task);

    Optional<Task> findByIdAndOwnerId(Long taskId, Long ownerId);

    void delete(Task task);
}
