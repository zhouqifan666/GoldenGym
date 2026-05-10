package com.goldengym.controller;

import com.goldengym.common.Result;
import com.goldengym.dto.ExerciseRecordDTO;
import com.goldengym.service.ExerciseService;
import com.goldengym.vo.ExerciseRecordVO;
import com.goldengym.vo.StatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping("/record")
    public Result<Void> addRecord(@Valid @RequestBody ExerciseRecordDTO dto) {
        return exerciseService.addRecord(dto);
    }

    @GetMapping("/records")
    public Result<Map<String, Object>> getRecords(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String type) {
        return exerciseService.getRecords(userId, page, size, type);
    }

    @DeleteMapping("/record/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        return exerciseService.deleteRecord(id);
    }

    @PutMapping("/record/{id}")
    public Result<Void> updateRecord(@PathVariable Long id, @Valid @RequestBody ExerciseRecordDTO dto) {
        return exerciseService.updateRecord(id, dto);
    }

    @GetMapping("/stats/week")
    public Result<StatsVO> getWeekStats(@RequestParam Long userId) {
        return exerciseService.getWeekStats(userId);
    }

    @GetMapping("/stats/month")
    public Result<StatsVO> getMonthStats(@RequestParam Long userId) {
        return exerciseService.getMonthStats(userId);
    }

    @GetMapping("/stats/total")
    public Result<StatsVO> getTotalStats(@RequestParam Long userId) {
        return exerciseService.getTotalStats(userId);
    }

    @GetMapping("/calendar/{year}/{month}")
    public Result<List<String>> getCalendarDates(
            @PathVariable int year,
            @PathVariable int month,
            @RequestParam Long userId) {
        return exerciseService.getCalendarDates(userId, year, month);
    }

    @GetMapping("/records/{date}")
    public Result<List<ExerciseRecordVO>> getRecordsByDate(
            @PathVariable String date,
            @RequestParam Long userId) {
        return exerciseService.getRecordsByDate(userId, date);
    }
}
