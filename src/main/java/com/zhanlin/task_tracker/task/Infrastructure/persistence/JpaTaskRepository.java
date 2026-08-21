package com.zhanlin.task_tracker.task.Infrastructure.persistence;

import com.zhanlin.task_tracker.identity.domain.User;
import com.zhanlin.task_tracker.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JpaTaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {


    Optional<Task> findByIdAndOwnerId(Long taskId, Long ownerId);

    Long owner(User owner);
}
