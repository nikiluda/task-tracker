package com.zhanlin.task_tracker.dto.UserDTO;

import jakarta.validation.constraints.Email;

public record UserResponse(

        Long id,
        String email
) {
}
