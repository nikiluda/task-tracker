package com.zhanlin.task_tracker.repository;

import com.zhanlin.task_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
