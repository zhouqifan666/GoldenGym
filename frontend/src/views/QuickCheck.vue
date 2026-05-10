<template>
  <div class="quick-check-page">
    <header class="page-header animate-in">
      <h1 class="page-title">快速打卡</h1>
      <p class="page-subtitle">选择运动类型，记录今天的训练</p>
    </header>

    <div class="type-grid">
      <div
        v-for="(t, i) in exerciseTypes"
        :key="t.value"
        class="type-card animate-in"
        :class="{ active: selectedType === t.value }"
        :style="{ animationDelay: (i * 0.06 + 0.1) + 's' }"
        @click="selectedType = t.value"
      >
        <div class="type-icon" :style="{ background: t.glow, color: t.color }">
          {{ t.icon }}
        </div>
        <span class="type-label">{{ t.label }}</span>
        <div class="type-check" v-if="selectedType === t.value">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="20 6 9 17 4 12" />
          </svg>
        </div>
      </div>
    </div>

    <div v-if="!selectedType" class="hint-state animate-in" style="animation-delay: 0.3s">
      <div class="hint-icon">⚡</div>
      <p class="hint-text">请选择运动类型开始打卡</p>
      <p class="hint-sub">没有找到想要的运动类型？未来版本将支持自定义</p>
    </div>

    <div v-if="selectedType" class="form-wrapper animate-in">
      <div class="form-card">
        <div class="form-header">
          <span class="form-type-badge" :style="{ background: activeType.glow, color: activeType.color }">
            {{ activeType.icon }} {{ selectedType }}
          </span>
        </div>

        <el-form :model="form" label-width="90px" ref="formRef" :rules="rules">
          <el-form-item label="运动日期" prop="date">
            <el-date-picker
              v-model="form.date"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="运动时长" prop="duration">
            <el-input-number v-model="form.duration" :min="1" placeholder="分钟" style="width: 100%" />
            <span class="unit-label">分钟</span>
          </el-form-item>
          <el-form-item label="消耗卡路里">
            <el-input-number v-model="form.calories" :min="0" placeholder="可选" style="width: 100%" />
            <span class="unit-label">kcal</span>
          </el-form-item>
          <el-form-item label="平均心率">
            <el-input-number v-model="form.avgHeartRate" :min="0" placeholder="可选" style="width: 100%" />
            <span class="unit-label">bpm</span>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.note" type="textarea" :rows="3" placeholder="记录训练感受..." />
          </el-form-item>
          <el-form-item>
            <button class="submit-btn" @click.prevent="handleSubmit" :disabled="loading">
              <span v-if="loading" class="btn-spinner"></span>
              <span v-else>⚡ 提交打卡</span>
            </button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { addRecord } from '../api/exercise'
import { useExerciseStore } from '../stores/exercise'
import { ElMessage } from 'element-plus'

const store = useExerciseStore()
const formRef = ref(null)
const loading = ref(false)
const selectedType = ref('')

const exerciseTypes = [
  { value: '有氧运动', label: '有氧运动', icon: '◆', color: '#10b981', glow: 'rgba(16, 185, 129, 0.12)' },
  { value: '力量训练', label: '力量训练', icon: '◈', color: '#f59e0b', glow: 'rgba(245, 158, 11, 0.12)' },
  { value: '功能性训练', label: '功能性训练', icon: '◎', color: '#ef4444', glow: 'rgba(239, 68, 68, 0.12)' }
]

const activeType = computed(() => exerciseTypes.find(t => t.value === selectedType.value) || exerciseTypes[0])

const today = new Date().toISOString().substring(0, 10)

const form = reactive({
  date: today,
  duration: null,
  calories: null,
  avgHeartRate: null,
  note: ''
})

const rules = {
  date: [{ required: true, message: '请选择运动日期', trigger: 'change' }],
  duration: [{ required: true, message: '请输入运动时长', trigger: 'blur' }]
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  loading.value = true
  try {
    await addRecord({
      userId: store.userId,
      type: selectedType.value,
      ...form
    })
    ElMessage.success('打卡成功！')
    form.date = today
    form.duration = null
    form.calories = null
    form.avgHeartRate = null
    form.note = ''
    selectedType.value = ''
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.quick-check-page {
  max-width: 680px;
  margin: 0 auto;
  padding: 28px;
}

.page-header {
  margin-bottom: 28px;
}

.page-title {
  font-family: var(--font-display);
  font-size: 28px;
  font-weight: 800;
  color: var(--text-primary);
  letter-spacing: -0.5px;
}

.page-subtitle {
  font-size: 14px;
  color: var(--text-muted);
  margin-top: 6px;
}

.type-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 24px;
}

.type-card {
  position: relative;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.type-card:hover {
  background: var(--bg-card-hover);
  border-color: var(--border-medium);
  transform: translateY(-2px);
}

.type-card.active {
  border-color: var(--accent);
  background: var(--accent-glow);
  box-shadow: 0 0 24px var(--accent-glow);
}

.type-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.type-label {
  font-family: var(--font-body);
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
}

.type-card.active .type-label {
  color: var(--accent);
  font-weight: 600;
}

.type-check {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 22px;
  height: 22px;
  background: var(--accent);
  color: #000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hint-state {
  text-align: center;
  padding: 48px 24px;
  background: var(--bg-card);
  border: 1px dashed var(--border-medium);
  border-radius: var(--radius-lg);
}

.hint-icon {
  font-size: 36px;
  margin-bottom: 12px;
  opacity: 0.5;
}

.hint-text {
  color: var(--text-secondary);
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
}

.hint-sub {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 6px;
}

.form-wrapper {
  margin-top: 4px;
}

.form-card {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 28px;
}

.form-header {
  margin-bottom: 24px;
}

.form-type-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  border-radius: var(--radius-full);
  font-size: 13px;
  font-weight: 600;
  font-family: var(--font-body);
}

.unit-label {
  margin-left: 8px;
  color: var(--text-muted);
  font-size: 13px;
}

.submit-btn {
  width: 100%;
  padding: 13px 24px;
  background: var(--accent);
  color: #000;
  border: none;
  border-radius: var(--radius-sm);
  font-family: var(--font-body);
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px var(--accent-glow-strong);
}

.submit-btn:hover:not(:disabled) {
  background: var(--accent-bright);
  transform: translateY(-1px);
  box-shadow: 0 6px 24px var(--accent-glow-strong);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(0, 0, 0, 0.2);
  border-top-color: #000;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
