package com.zhanlin.task_tracker.task.application.service;


import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TaskSortValidator {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "title",
            "status",
            "createdAt",
            "doneAt"
    );


    public void validate(Pageable pageable) {
        pageable.getSort().forEach(order -> {
            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new IllegalArgumentException(
                        "Сортировка по полю '" + order.getProperty() + "' не поддерживается"
                );
            }
        });
    }
}


