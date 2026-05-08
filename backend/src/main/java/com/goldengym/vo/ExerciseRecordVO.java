package com.goldengym.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExerciseRecordVO {
    private Long id;
    private String type;
    private LocalDate date;
    private Integer duration;
    private Integer calories;
    private Integer avgHeartRate;
    private String note;
    private LocalDateTime createTime;
}
