<template>
  <el-popover
    placement="top"
    :width="280"
    trigger="hover"
    :show-after="300"
    :disabled="!record.note"
    popper-class="note-popover"
  >
    <template #reference>
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
            <button class="action-btn edit-btn" @click="$emit('edit', record)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" /><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
              </svg>
            </button>
            <button class="action-btn delete-btn" @click="$emit('delete', record.id)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="3 6 5 6 21 6" /><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </template>
    <div class="note-popover-content">
      <div class="note-text">{{ record.note }}</div>
    </div>
  </el-popover>
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

defineEmits(['delete', 'edit'])

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

.card-right {
  display: flex;
  gap: 4px;
}

.action-btn {
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

.edit-btn:hover {
  background: rgba(99, 102, 241, 0.1);
  border-color: rgba(99, 102, 241, 0.2);
  color: #6366f1;
}

.delete-btn:hover {
  background: var(--danger-glow);
  border-color: rgba(239, 68, 68, 0.2);
  color: var(--danger);
}
</style>

<style>
/* ── Molten Gold Popover ── */
.el-popover.note-popover {
  --gold-1: #f6c67a;
  --gold-2: #e6a84d;
  --gold-3: #c8872e;
  --gold-4: #a0651a;
  --gold-deep: #6b3e10;

  background:
    radial-gradient(ellipse 80% 60% at 30% 20%, rgba(255, 230, 160, 0.45), transparent 60%),
    linear-gradient(160deg, var(--gold-1) 0%, var(--gold-2) 28%, var(--gold-3) 55%, var(--gold-4) 80%, var(--gold-deep) 100%) !important;
  border: 1px solid rgba(255, 215, 120, 0.35) !important;
  border-radius: 14px !important;
  box-shadow:
    0 4px 12px rgba(160, 101, 26, 0.25),
    0 12px 40px rgba(107, 62, 16, 0.35),
    inset 0 1px 0 rgba(255, 240, 180, 0.5) !important;
  overflow: hidden;
  padding: 0 !important;
}

/* shimmer sweep */
.el-popover.note-popover::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(
    105deg,
    transparent 30%,
    rgba(255, 250, 220, 0.25) 45%,
    rgba(255, 250, 220, 0.08) 55%,
    transparent 70%
  );
  transform: translateX(-100%);
  animation: gold-shimmer 2.8s ease-in-out infinite;
  pointer-events: none;
  z-index: 1;
}

@keyframes gold-shimmer {
  0%, 100% { transform: translateX(-100%); }
  50% { transform: translateX(100%); }
}

/* grain texture */
.el-popover.note-popover::after {
  content: '';
  position: absolute;
  inset: 0;
  opacity: 0.06;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E");
  background-size: 100px 100px;
  pointer-events: none;
  z-index: 1;
  border-radius: inherit;
}

/* arrow — cover both Element Plus class variants */
.el-popover.note-popover .el-popper__arrow::before,
.el-popover.note-popover .el-popover__arrow::before {
  background: linear-gradient(160deg, var(--gold-2), var(--gold-4)) !important;
  border: 1px solid rgba(255, 215, 120, 0.35) !important;
  border-right: none !important;
  border-bottom: none !important;
}

/* content */
.note-popover-content {
  position: relative;
  z-index: 2;
  padding: 16px 18px;
}

.note-text {
  font-size: 13.5px;
  color: #fff;
  font-weight: 500;
  line-height: 1.75;
  white-space: pre-line;
  word-break: break-word;
  text-shadow: 0 1px 3px rgba(107, 62, 16, 0.3);
}
</style>
