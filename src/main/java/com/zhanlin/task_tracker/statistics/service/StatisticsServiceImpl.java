package com.zhanlin.task_tracker.statistics.service;


import com.zhanlin.task_tracker.entity.User;
import com.zhanlin.task_tracker.service.CurrentUserService;
import com.zhanlin.task_tracker.statistics.api.response.CompletionStatistics;
import com.zhanlin.task_tracker.statistics.api.response.StatisticsResponse;
import com.zhanlin.task_tracker.statistics.api.response.TasksStatistics;
import com.zhanlin.task_tracker.statistics.infrastructure.repository.StatisticsRepository;
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
