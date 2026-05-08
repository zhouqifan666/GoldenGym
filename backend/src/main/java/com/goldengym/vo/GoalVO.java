package com.goldengym.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoalVO {
    private Long id;
    private String period;
    private Integer targetDays;
    private Integer targetDuration;
    private Integer targetCalories;
    private Boolean completed;
    private LocalDateTime createTime;
}
