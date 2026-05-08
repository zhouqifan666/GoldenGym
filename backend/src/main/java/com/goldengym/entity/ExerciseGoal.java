package com.goldengym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exercise_goal")
public class ExerciseGoal {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String period;
    private Integer targetDays;
    private Integer targetDuration;
    private Integer targetCalories;
    private Boolean completed;
    private LocalDateTime createTime;
}
