package com.goldengym.service;

import com.goldengym.common.Result;
import com.goldengym.dto.GoalDTO;
import com.goldengym.vo.GoalVO;

import java.util.List;

public interface GoalService {
    Result<Void> createGoal(GoalDTO dto);
    Result<List<GoalVO>> listGoals(Long userId);
    Result<Void> toggleComplete(Long id);
    Result<Void> deleteGoal(Long id);
}
