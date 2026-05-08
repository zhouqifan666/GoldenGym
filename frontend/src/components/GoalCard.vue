<template>
  <div class="goal-card" :class="{ completed: goal.completed }">
    <div class="goal-header">
      <el-tag :type="periodType" effect="plain">{{ goal.period }}目标</el-tag>
      <el-checkbox
        :model-value="goal.completed"
        @change="$emit('toggle', goal.id)"
      />
    </div>
    <div class="goal-body">
      <div class="goal-item" v-if="goal.targetDays">
        <span class="goal-label">目标天数</span>
        <span class="goal-value">{{ goal.targetDays }}天</span>
      </div>
      <div class="goal-item" v-if="goal.targetDuration">
        <span class="goal-label">目标时长</span>
        <span class="goal-value">{{ goal.targetDuration }}分钟</span>
      </div>
      <div class="goal-item" v-if="goal.targetCalories">
        <span class="goal-label">目标卡路里</span>
        <span class="goal-value">{{ goal.targetCalories }}kcal</span>
      </div>
    </div>
    <div class="goal-footer">
      <span class="goal-time">{{ formatDate(goal.createTime) }}</span>
      <el-button type="danger" link @click="$emit('delete', goal.id)">
        <el-icon><Delete /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  goal: Object
})

defineEmits(['toggle', 'delete'])

const periodType = computed(() => {
  const types = { '每日': 'success', '每周': 'warning', '每月': 'danger' }
  return types[props.goal.period] || 'info'
})

function formatDate(dateStr) {
  if (!dateStr) return ''
  return dateStr.substring(0, 10)
}
</script>

<style scoped>
.goal-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.goal-card.completed {
  opacity: 0.6;
}

.goal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.goal-body {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 16px;
}

.goal-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.goal-label {
  font-size: 12px;
  color: #999;
}

.goal-value {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.goal-card.completed .goal-value {
  text-decoration: line-through;
}

.goal-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.goal-time {
  font-size: 12px;
  color: #ccc;
}
</style>
