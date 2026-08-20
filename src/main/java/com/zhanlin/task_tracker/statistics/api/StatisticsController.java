package com.zhanlin.task_tracker.statistics.api;

import com.zhanlin.task_tracker.statistics.api.response.StatisticsResponse;
import com.zhanlin.task_tracker.statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Statistics",
        description = "Task statistics for the authenticated user"
)
@RestController
@RequestMapping("/statistics")
@SecurityRequirement(name = "bearerAuth")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Operation(
            summary = "Get task statistics",
            description = "Returns task statistics for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statistics successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            )
    })
    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics() {
        return ResponseEntity.ok(
                statisticsService.getStatistics()
        );
    }
}