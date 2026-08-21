package com.zhanlin.task_tracker.task.Infrastructure.persistence;

import com.zhanlin.task_tracker.task.application.model.TaskPage;
import com.zhanlin.task_tracker.task.application.model.TaskPageRequest;
import com.zhanlin.task_tracker.task.application.model.TaskSearchCriteria;
import com.zhanlin.task_tracker.task.application.port.out.TaskQueryPort;
import com.zhanlin.task_tracker.task.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;


@Repository
public class TaskQueryAdapter  implements TaskQueryPort {

    private final JpaTaskRepository repository;

    public TaskQueryAdapter(JpaTaskRepository repository) {
        this.repository = repository;
    }


    @Override
    public TaskPage findTasks(TaskSearchCriteria criteria, TaskPageRequest pageRequest) {
        Specification<Task> specification =
                TaskSpecification.belongsToOwner(criteria.ownerId());

        if (criteria.status() != null) {
            specification = specification.and(
                    TaskSpecification.hasStatus(criteria.status())
            );
        }

        if (criteria.title() != null) {
            specification = specification.and(
                    TaskSpecification.titleContains(criteria.title())
            );
        }

        Pageable pageable = PageRequest.of(
                pageRequest.page(),
                pageRequest.size(),
                Sort.by(
                        Sort.Direction.fromString(pageRequest.direction()),
                        pageRequest.sortBy()
                )
        );

        Page<Task> page =
                repository.findAll(specification, pageable);

        return new TaskPage(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
