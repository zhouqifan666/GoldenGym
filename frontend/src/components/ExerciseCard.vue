<template>
  <div class="exercise-card">
    <div class="card-stripe" :style="{ background: accentColor }"></div>
    <div class="card-body">
      <div class="card-left">
        <div class="type-badge" :style="{ background: accentGlow, color: accentColor }">
          {{ record.type }}
        </div>
        <div class="card-date">{{ record.date }}</div>
      </div>
      <div class="card-center">
        <div class="metric">
          <span class="metric-icon">◷</span>
          <span class="metric-value">{{ record.duration }}</span>
          <span class="metric-unit">分钟</span>
        </div>
        <div class="metric" v-if="record.calories">
          <span class="metric-icon">◈</span>
          <span class="metric-value">{{ record.calories }}</span>
          <span class="metric-unit">kcal</span>
        </div>
        <div class="metric" v-if="record.avgHeartRate">
          <span class="metric-icon">♡</span>
          <span class="metric-value">{{ record.avgHeartRate }}</span>
          <span class="metric-unit">bpm</span>
        </div>
      </div>
      <div class="card-right" v-if="showDelete">
        <button class="delete-btn" @click="$emit('delete', record.id)">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="3 6 5 6 21 6" /><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  record: Object,
  showDelete: {
    type: Boolean,
    default: false
  }
})

defineEmits(['delete'])

const colorMap = {
  '有氧运动': { color: '#10b981', glow: 'rgba(16, 185, 129, 0.12)' },
  '力量训练': { color: '#f59e0b', glow: 'rgba(245, 158, 11, 0.12)' },
  '功能性训练': { color: '#ef4444', glow: 'rgba(239, 68, 68, 0.12)' }
}

const accentColor = computed(() => colorMap[props.record.type]?.color || '#6366f1')
const accentGlow = computed(() => colorMap[props.record.type]?.glow || 'rgba(99, 102, 241, 0.12)')
</script>

<style scoped>
.exercise-card {
  position: relative;
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  margin-bottom: 8px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.exercise-card:hover {
  background: var(--bg-card-hover);
  border-color: var(--border-medium);
  transform: translateX(4px);
}

.card-stripe {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  border-radius: 3px 0 0 3px;
}

.card-body {
  display: flex;
  align-items: center;
  padding: 16px 20px 16px 24px;
  gap: 20px;
}

.card-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 100px;
}

.type-badge {
  display: inline-flex;
  padding: 4px 10px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 600;
  font-family: var(--font-body);
  letter-spacing: 0.3px;
  width: fit-content;
}

.card-date {
  font-size: 12px;
  color: var(--text-muted);
  font-weight: 400;
}

.card-center {
  flex: 1;
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.metric {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.metric-icon {
  font-size: 11px;
  color: var(--text-muted);
  margin-right: 2px;
}

.metric-value {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.5px;
}

.metric-unit {
  font-size: 12px;
  color: var(--text-muted);
  font-weight: 400;
}

.delete-btn {
  width: 32px;
  height: 32px;
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

.delete-btn:hover {
  background: var(--danger-glow);
  border-color: rgba(239, 68, 68, 0.2);
  color: var(--danger);
}
</style>
