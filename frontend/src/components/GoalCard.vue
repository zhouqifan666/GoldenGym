<template>
  <div class="goal-card" :class="{ completed: goal.completed }">
    <div class="goal-header">
      <div class="period-badge" :class="goal.period">
        {{ goal.period }}
      </div>
      <label class="custom-check" :class="{ checked: goal.completed }">
        <input
          type="checkbox"
          :checked="goal.completed"
          @change="$emit('toggle', goal.id)"
        />
        <span class="check-mark">
          <svg v-if="goal.completed" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="20 6 9 17 4 12" />
          </svg>
        </span>
      </label>
    </div>

    <div class="goal-metrics">
      <div class="goal-metric" v-if="goal.targetDays">
        <span class="metric-label">目标天数</span>
        <span class="metric-num">{{ goal.targetDays }}<small>天</small></span>
      </div>
      <div class="goal-metric" v-if="goal.targetDuration">
        <span class="metric-label">目标时长</span>
        <span class="metric-num">{{ goal.targetDuration }}<small>分钟</small></span>
      </div>
      <div class="goal-metric" v-if="goal.targetCalories">
        <span class="metric-label">目标卡路里</span>
        <span class="metric-num">{{ goal.targetCalories }}<small>kcal</small></span>
      </div>
    </div>

    <div class="goal-footer">
      <span class="goal-date">{{ formatDate(goal.createTime) }}</span>
      <button class="del-btn" @click="$emit('delete', goal.id)">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  goal: Object
})

defineEmits(['toggle', 'delete'])

function formatDate(dateStr) {
  if (!dateStr) return ''
  return dateStr.substring(0, 10)
}
</script>

<style scoped>
.goal-card {
  position: relative;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.goal-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--accent), var(--accent-dim));
  opacity: 0.5;
  transition: opacity 0.3s;
}

.goal-card:hover {
  background: var(--bg-card-hover);
  border-color: var(--border-medium);
  transform: translateY(-2px);
  box-shadow: var(--shadow-card);
}

.goal-card.completed {
  opacity: 0.5;
}

.goal-card.completed::before {
  opacity: 0.15;
}

.goal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}

.period-badge {
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 600;
  font-family: var(--font-body);
  letter-spacing: 0.5px;
}

.period-badge.每日 {
  background: var(--success-glow);
  color: var(--success);
}

.period-badge.每周 {
  background: var(--accent-glow);
  color: var(--accent);
}

.period-badge.每月 {
  background: var(--info-glow);
  color: var(--info);
}

.custom-check {
  position: relative;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.custom-check input {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.check-mark {
  width: 22px;
  height: 22px;
  border-radius: 6px;
  border: 2px solid var(--border-medium);
  background: var(--bg-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.25s;
}

.custom-check.checked .check-mark {
  background: var(--accent);
  border-color: var(--accent);
  color: #000;
  box-shadow: 0 2px 8px var(--accent-glow-strong);
}

.goal-metrics {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 18px;
}

.goal-metric {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.metric-label {
  font-size: 11px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.8px;
  font-weight: 500;
}

.metric-num {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.5px;
}

.metric-num small {
  font-size: 12px;
  font-weight: 400;
  color: var(--text-muted);
  margin-left: 2px;
}

.goal-card.completed .metric-num {
  text-decoration: line-through;
  opacity: 0.6;
}

.goal-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 14px;
  border-top: 1px solid var(--border-subtle);
}

.goal-date {
  font-size: 12px;
  color: var(--text-muted);
}

.del-btn {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-sm);
  border: 1px solid transparent;
  background: transparent;
  color: var(--text-muted);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.del-btn:hover {
  background: var(--danger-glow);
  border-color: rgba(239, 68, 68, 0.2);
  color: var(--danger);
}
</style>
