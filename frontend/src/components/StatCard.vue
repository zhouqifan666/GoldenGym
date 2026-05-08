<template>
  <div class="stat-card" :style="{ borderTop: `3px solid ${color}` }">
    <div class="stat-header">
      <span class="stat-title">{{ title }}</span>
      <el-icon :size="24" :style="{ color }">
        <component :is="icon" />
      </el-icon>
    </div>
    <div class="stat-value" :style="{ color }">{{ value }}</div>
    <div class="stat-sub">{{ subtitle }}</div>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  value: [String, Number],
  subtitle: String,
  icon: String,
  color: {
    type: String,
    default: '#F4672A'
  }
})
</script>

<style scoped>
.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.stat-title {
  font-size: 14px;
  color: #999;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-sub {
  font-size: 13px;
  color: #999;
}
</style>
