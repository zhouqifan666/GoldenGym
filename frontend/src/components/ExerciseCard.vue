<template>
  <div class="exercise-card">
    <div class="card-left">
      <el-tag :type="tagType" effect="dark" size="small">{{ record.type }}</el-tag>
      <div class="card-date">{{ record.date }}</div>
    </div>
    <div class="card-center">
      <div class="card-info">
        <span class="info-item">
          <el-icon><Timer /></el-icon>
          {{ record.duration }}分钟
        </span>
        <span class="info-item" v-if="record.calories">
          <el-icon><Flame /></el-icon>
          {{ record.calories }}kcal
        </span>
        <span class="info-item" v-if="record.avgHeartRate">
          <el-icon><Heart /></el-icon>
          {{ record.avgHeartRate }}bpm
        </span>
      </div>
    </div>
    <div class="card-right" v-if="showDelete">
      <el-button type="danger" link @click="$emit('delete', record.id)">
        <el-icon><Delete /></el-icon>
      </el-button>
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

const tagType = computed(() => {
  const types = {
    '有氧运动': 'success',
    '力量训练': 'warning',
    '功能性训练': 'danger'
  }
  return types[props.record.type] || 'info'
})
</script>

<style scoped>
.exercise-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  margin-bottom: 10px;
}

.card-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 100px;
}

.card-date {
  font-size: 13px;
  color: #999;
}

.card-center {
  flex: 1;
  padding: 0 20px;
}

.card-info {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #666;
}
</style>
