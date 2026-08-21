package com.zhanlin.task_tracker.task.Infrastructure.persistence;

import com.zhanlin.task_tracker.task.application.port.out.TaskRepositoryPort;
import com.zhanlin.task_tracker.task.domain.Task;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository repository;

    public TaskRepositoryAdapter(JpaTaskRepository repository) {
        this.repository = repository;
    }


    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public Optional<Task> findByIdAndOwnerId(Long taskId, Long ownerId) {
        return repository.findByIdAndOwnerId(taskId, ownerId);
    }

    @Override
    public void delete(Task task) {
        repository.delete(task);
    }
}
