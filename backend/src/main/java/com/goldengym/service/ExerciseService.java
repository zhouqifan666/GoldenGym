package com.goldengym.service;

import com.goldengym.common.Result;
import com.goldengym.dto.ExerciseRecordDTO;
import com.goldengym.vo.ExerciseRecordVO;
import com.goldengym.vo.StatsVO;

import java.util.List;
import java.util.Map;

public interface ExerciseService {
    Result<Void> addRecord(ExerciseRecordDTO dto);
    Result<Map<String, Object>> getRecords(Long userId, Integer page, Integer size, String type);
    Result<StatsVO> getWeekStats(Long userId);
    Result<StatsVO> getMonthStats(Long userId);
    Result<StatsVO> getTotalStats(Long userId);
    Result<List<String>> getCalendarDates(Long userId, int year, int month);
    Result<List<ExerciseRecordVO>> getRecordsByDate(Long userId, String date);
    Result<Void> deleteRecord(Long id);
    Result<Void> updateRecord(Long id, ExerciseRecordDTO dto);
}
