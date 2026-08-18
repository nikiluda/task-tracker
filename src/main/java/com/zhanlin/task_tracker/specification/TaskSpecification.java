package com.zhanlin.task_tracker.specification;

import com.zhanlin.task_tracker.entity.Task;
import com.zhanlin.task_tracker.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecification {
    private TaskSpecification() {
    }

    public static Specification<Task> belongsToOwner(Long ownerId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("owner").get("id"),
                        ownerId
                );
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Task> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                );
    }


}
