package com.zhanlin.task_tracker.repository;

import com.zhanlin.task_tracker.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

//    Page<Task> findAllByOwnerId(Long id, Pageable pageable);

    Optional<Task> findByIdAndOwnerId(Long taskId, Long ownerId);
}
