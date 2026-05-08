package com.goldengym.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GoalDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "目标周期不能为空")
    private String period;

    private Integer targetDays;
    private Integer targetDuration;
    private Integer targetCalories;
}
