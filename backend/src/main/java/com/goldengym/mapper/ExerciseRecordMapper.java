package com.goldengym.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goldengym.entity.ExerciseRecord;
import com.goldengym.vo.StatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ExerciseRecordMapper extends BaseMapper<ExerciseRecord> {

    @Select("SELECT DISTINCT date FROM exercise_record WHERE user_id = #{userId} AND YEAR(date) = #{year} AND MONTH(date) = #{month}")
    List<LocalDate> selectCalendarDates(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    @Select("SELECT COUNT(*) as count, COUNT(DISTINCT date) as days, IFNULL(SUM(duration),0) as totalDuration, IFNULL(SUM(calories),0) as totalCalories " +
            "FROM exercise_record WHERE user_id = #{userId} AND date >= #{startDate} AND date <= #{endDate}")
    StatsVO selectStatsByDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Select("SELECT COUNT(*) as count, IFNULL(SUM(duration),0) as totalDuration, IFNULL(SUM(calories),0) as totalCalories " +
            "FROM exercise_record WHERE user_id = #{userId}")
    StatsVO selectTotalStats(@Param("userId") Long userId);
}
