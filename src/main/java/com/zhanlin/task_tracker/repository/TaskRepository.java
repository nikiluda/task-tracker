package com.zhanlin.task_tracker.repository;

import com.zhanlin.task_tracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
