package com.goldengym.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ExerciseRecordDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "运动类型不能为空")
    private String type;

    @NotNull(message = "运动日期不能为空")
    private LocalDate date;

    @NotNull(message = "运动时长不能为空")
    @Min(value = 1, message = "运动时长必须大于0")
    private Integer duration;

    private Integer calories;
    private Integer avgHeartRate;
    private String note;
}
