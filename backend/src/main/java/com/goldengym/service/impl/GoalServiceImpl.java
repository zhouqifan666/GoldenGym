package com.goldengym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.goldengym.common.Result;
import com.goldengym.dto.GoalDTO;
import com.goldengym.entity.ExerciseGoal;
import com.goldengym.mapper.ExerciseGoalMapper;
import com.goldengym.service.GoalService;
import com.goldengym.vo.GoalVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private ExerciseGoalMapper exerciseGoalMapper;

    @Override
    public Result<Void> createGoal(GoalDTO dto) {
        ExerciseGoal goal = new ExerciseGoal();
        BeanUtils.copyProperties(dto, goal);
        goal.setCompleted(false);
        exerciseGoalMapper.insert(goal);
        return Result.success();
    }

    @Override
    public Result<List<GoalVO>> listGoals(Long userId) {
        LambdaQueryWrapper<ExerciseGoal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseGoal::getUserId, userId)
                .orderByDesc(ExerciseGoal::getCreateTime);
        List<ExerciseGoal> goals = exerciseGoalMapper.selectList(wrapper);
        List<GoalVO> voList = goals.stream().map(g -> {
            GoalVO vo = new GoalVO();
            BeanUtils.copyProperties(g, vo);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public Result<Void> toggleComplete(Long id) {
        ExerciseGoal goal = exerciseGoalMapper.selectById(id);
        if (goal == null) {
            return Result.fail("目标不存在");
        }
        goal.setCompleted(!goal.getCompleted());
        exerciseGoalMapper.updateById(goal);
        return Result.success();
    }

    @Override
    public Result<Void> deleteGoal(Long id) {
        exerciseGoalMapper.deleteById(id);
        return Result.success();
    }
}
