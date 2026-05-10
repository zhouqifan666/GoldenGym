<template>
  <div class="stat-card" :style="{ '--card-accent': color, '--card-glow': color + '20' }">
    <div class="stat-bg"></div>
    <div class="stat-content">
      <div class="stat-header">
        <span class="stat-title">{{ title }}</span>
        <div class="stat-icon-badge">
          <span class="stat-icon-text">{{ iconChar }}</span>
        </div>
      </div>
      <div class="stat-value">{{ value }}</div>
      <div class="stat-sub">{{ subtitle }}</div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: String,
  value: [String, Number],
  subtitle: String,
  icon: String,
  color: {
    type: String,
    default: '#f59e0b'
  }
})

const iconMap = {
  'Calendar': '◈',
  'TrendCharts': '◆',
  'Aim': '◎',
  'Timer': '◷',
  'Flame': '◈'
}

const iconChar = computed(() => iconMap[props.icon] || '◆')
</script>

<style scoped>
.stat-card {
  position: relative;
  border-radius: var(--radius-lg);
  overflow: hidden;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 32px var(--card-glow);
}

.stat-bg {
  position: absolute;
  inset: 0;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
}

.stat-bg::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--card-accent), transparent);
  opacity: 0.6;
}

.stat-bg::after {
  content: '';
  position: absolute;
  top: -40px;
  right: -40px;
  width: 120px;
  height: 120px;
  background: radial-gradient(circle, var(--card-glow) 0%, transparent 70%);
  border-radius: 50%;
}

.stat-content {
  position: relative;
  z-index: 1;
  padding: 20px;
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.stat-title {
  font-family: var(--font-body);
  font-size: 12px;
  font-weight: 500;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 1px;
}

.stat-icon-badge {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-sm);
  background: var(--card-glow);
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon-text {
  font-size: 14px;
  color: var(--card-accent);
}

.stat-value {
  font-family: var(--font-display);
  font-size: 32px;
  font-weight: 800;
  color: var(--text-primary);
  line-height: 1;
  margin-bottom: 6px;
  letter-spacing: -1px;
}

.stat-sub {
  font-family: var(--font-body);
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 400;
}
</style>
