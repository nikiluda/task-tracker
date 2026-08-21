package com.zhanlin.task_tracker.task.Infrastructure.persistence;

import com.zhanlin.task_tracker.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

//    Page<Task> findAllByOwnerId(Long id, Pageable pageable);

    Optional<Task> findByIdAndOwnerId(Long taskId, Long ownerId);
}
