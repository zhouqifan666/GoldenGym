package com.goldengym.controller;

import com.goldengym.common.Result;
import com.goldengym.dto.GoalDTO;
import com.goldengym.service.GoalService;
import com.goldengym.vo.GoalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/goal")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping("/create")
    public Result<Void> createGoal(@Valid @RequestBody GoalDTO dto) {
        return goalService.createGoal(dto);
    }

    @GetMapping("/list")
    public Result<List<GoalVO>> listGoals(@RequestParam Long userId) {
        return goalService.listGoals(userId);
    }

    @PutMapping("/{id}/complete")
    public Result<Void> toggleComplete(@PathVariable Long id) {
        return goalService.toggleComplete(id);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteGoal(@PathVariable Long id) {
        return goalService.deleteGoal(id);
    }
}
