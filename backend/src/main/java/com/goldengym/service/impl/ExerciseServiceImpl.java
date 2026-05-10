package com.goldengym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goldengym.common.Result;
import com.goldengym.dto.ExerciseRecordDTO;
import com.goldengym.entity.ExerciseRecord;
import com.goldengym.mapper.ExerciseRecordMapper;
import com.goldengym.service.ExerciseService;
import com.goldengym.vo.ExerciseRecordVO;
import com.goldengym.vo.StatsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @Override
    public Result<Void> addRecord(ExerciseRecordDTO dto) {
        ExerciseRecord record = new ExerciseRecord();
        BeanUtils.copyProperties(dto, record);
        exerciseRecordMapper.insert(record);
        return Result.success();
    }

    @Override
    public Result<Map<String, Object>> getRecords(Long userId, Integer page, Integer size, String type) {
        Page<ExerciseRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ExerciseRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseRecord::getUserId, userId);
        if (StringUtils.hasText(type)) {
            wrapper.eq(ExerciseRecord::getType, type);
        }
        wrapper.orderByDesc(ExerciseRecord::getDate);
        exerciseRecordMapper.selectPage(pageParam, wrapper);

        List<ExerciseRecordVO> voList = pageParam.getRecords().stream().map(r -> {
            ExerciseRecordVO vo = new ExerciseRecordVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("records", voList);
        result.put("total", pageParam.getTotal());
        result.put("page", page);
        result.put("size", size);
        return Result.success(result);
    }

    @Override
    public Result<StatsVO> getWeekStats(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        StatsVO stats = exerciseRecordMapper.selectStatsByDateRange(userId, startOfWeek, endOfWeek);
        return Result.success(stats);
    }

    @Override
    public Result<StatsVO> getMonthStats(Long userId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();
        StatsVO stats = exerciseRecordMapper.selectStatsByDateRange(userId, startOfMonth, endOfMonth);
        return Result.success(stats);
    }

    @Override
    public Result<StatsVO> getTotalStats(Long userId) {
        StatsVO stats = exerciseRecordMapper.selectTotalStats(userId);
        return Result.success(stats);
    }

    @Override
    public Result<List<String>> getCalendarDates(Long userId, int year, int month) {
        List<LocalDate> dates = exerciseRecordMapper.selectCalendarDates(userId, year, month);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> dateStrings = dates.stream()
                .map(d -> d.format(formatter))
                .collect(Collectors.toList());
        return Result.success(dateStrings);
    }

    @Override
    public Result<List<ExerciseRecordVO>> getRecordsByDate(Long userId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LambdaQueryWrapper<ExerciseRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseRecord::getUserId, userId)
                .eq(ExerciseRecord::getDate, localDate)
                .orderByDesc(ExerciseRecord::getCreateTime);
        List<ExerciseRecord> records = exerciseRecordMapper.selectList(wrapper);
        List<ExerciseRecordVO> voList = records.stream().map(r -> {
            ExerciseRecordVO vo = new ExerciseRecordVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public Result<Void> deleteRecord(Long id) {
        exerciseRecordMapper.deleteById(id);
        return Result.success();
    }

    @Override
    public Result<Void> updateRecord(Long id, ExerciseRecordDTO dto) {
        ExerciseRecord record = exerciseRecordMapper.selectById(id);
        if (record == null) {
            return Result.fail("记录不存在");
        }
        if (dto.getType() != null) record.setType(dto.getType());
        if (dto.getDate() != null) record.setDate(dto.getDate());
        if (dto.getDuration() != null) record.setDuration(dto.getDuration());
        if (dto.getCalories() != null) record.setCalories(dto.getCalories());
        if (dto.getAvgHeartRate() != null) record.setAvgHeartRate(dto.getAvgHeartRate());
        if (dto.getNote() != null) record.setNote(dto.getNote());
        exerciseRecordMapper.updateById(record);
        return Result.success();
    }
}
