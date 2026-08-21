package com.zhanlin.task_tracker.analytics.infrastructure.persistence;


import com.zhanlin.task_tracker.analytics.api.rest.dto.CompletionStatistics;
import com.zhanlin.task_tracker.analytics.api.rest.dto.TasksStatistics;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class StatisticsRepository {

    private final JdbcTemplate jdbcTemplate;


    public StatisticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TasksStatistics getTasksStatistics(Long ownerId) {
        return jdbcTemplate.queryForObject(
                """
                SELECT
                    COUNT(*) AS total,
                    COUNT(*) FILTER (WHERE status = 'WAITING') AS waiting,
                    COUNT(*) FILTER (WHERE status = 'DONE') AS done
                FROM tasks
                WHERE owner_id = ?
                """,
                (resultSet, rowNum) ->
                        new TasksStatistics(
                                resultSet.getLong("total"),
                                resultSet.getLong("waiting"),
                                resultSet.getLong("done")
                        ),
                ownerId
        );
    }

    public CompletionStatistics getCompletionStatistics(Long ownerId) {
        return jdbcTemplate.queryForObject(
                """
                        SELECT 
                        AVG(EXTRACT(EPOCH FROM (done_at - created_at))) AS average_completion_time
                        FROM tasks
                        WHERE owner_id = ? AND done_at IS NOT NULL
                        """,
                (rs, rowNum) -> {
                    Number value = (Number) rs.getObject("average_completion_time");

                    Long averageCompletionTimeSeconds =
                            value == null ? 0L : Math.round(value.doubleValue());

                    return new CompletionStatistics(
                            averageCompletionTimeSeconds
                    );
                },
                ownerId
        );
    }
}
