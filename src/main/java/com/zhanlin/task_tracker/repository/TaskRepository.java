package com.zhanlin.task_tracker.repository;

import com.zhanlin.task_tracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOwnerId(Long id);

    Optional<Task> findByIdAndOwnerId(Long taskId, Long ownerId);
}
