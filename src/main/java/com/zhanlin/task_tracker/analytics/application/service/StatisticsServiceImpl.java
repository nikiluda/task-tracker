package com.zhanlin.task_tracker.analytics.application.service;


import com.zhanlin.task_tracker.identity.domain.User;
import com.zhanlin.task_tracker.identity.application.service.CurrentUserService;
import com.zhanlin.task_tracker.analytics.api.rest.dto.CompletionStatistics;
import com.zhanlin.task_tracker.analytics.api.rest.dto.StatisticsResponse;
import com.zhanlin.task_tracker.analytics.api.rest.dto.TasksStatistics;
import com.zhanlin.task_tracker.analytics.infrastructure.persistence.StatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {


    private final StatisticsRepository statisticsRepository;
    private final CurrentUserService currentUserService;

    public StatisticsServiceImpl(StatisticsRepository statisticsRepository, CurrentUserService currentUserService) {
        this.statisticsRepository = statisticsRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics() {
        User currentUser = currentUserService.getCurrentUser();

        TasksStatistics tasksStatistics =
                statisticsRepository.getTasksStatistics(currentUser.getId());

        CompletionStatistics completionStatistics =
                statisticsRepository.getCompletionStatistics(currentUser.getId());

        return new StatisticsResponse(
                tasksStatistics,
                completionStatistics
        );
    }


}
