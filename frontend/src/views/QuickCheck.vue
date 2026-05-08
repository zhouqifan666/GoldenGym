<template>
  <div class="quick-check-page">
    <h1 class="page-title">快速打卡</h1>

    <div class="type-selector">
      <div
        v-for="t in exerciseTypes"
        :key="t.value"
        class="type-card"
        :class="{ active: selectedType === t.value }"
        @click="selectedType = t.value"
      >
        <el-icon :size="32"><component :is="t.icon" /></el-icon>
        <span>{{ t.label }}</span>
      </div>
    </div>

    <div class="type-hint" v-if="!selectedType">
      <el-icon :size="48" color="#ddd"><Plus /></el-icon>
      <p>请选择运动类型开始打卡</p>
      <p class="hint-sub">没有找到想要的运动类型？未来版本将支持自定义运动类型</p>
    </div>

    <el-card v-if="selectedType" class="form-card">
      <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="运动类型">
          <el-tag effect="dark">{{ selectedType }}</el-tag>
        </el-form-item>
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
          <span class="unit">分钟</span>
        </el-form-item>
        <el-form-item label="消耗卡路里">
          <el-input-number v-model="form.calories" :min="0" placeholder="可选" style="width: 100%" />
          <span class="unit">kcal</span>
        </el-form-item>
        <el-form-item label="平均心率">
          <el-input-number v-model="form.avgHeartRate" :min="0" placeholder="可选" style="width: 100%" />
          <span class="unit">bpm</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.note" type="textarea" :rows="3" placeholder="可选备注" />
        </el-form-item>
        <el-form-item>
          <el-button type="warning" @click="handleSubmit" :loading="loading" style="width: 100%">
            提交打卡
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { addRecord } from '../api/exercise'
import { useExerciseStore } from '../stores/exercise'
import { ElMessage } from 'element-plus'

const store = useExerciseStore()
const formRef = ref(null)
const loading = ref(false)
const selectedType = ref('')

const exerciseTypes = [
  { value: '有氧运动', label: '有氧运动', icon: 'TrendCharts' },
  { value: '力量训练', label: '力量训练', icon: 'Bicycle' },
  { value: '功能性训练', label: '功能性训练', icon: 'Star' }
]

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
  max-width: 700px;
  margin: 0 auto;
  padding: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 24px;
}

.type-selector {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.type-card {
  background: #fff;
  border: 2px solid #eee;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.type-card:hover {
  border-color: #F4672A;
}

.type-card.active {
  border-color: #F4672A;
  color: #F4672A;
}

.type-hint {
  text-align: center;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  color: #999;
}

.hint-sub {
  font-size: 13px;
  color: #ccc;
  margin-top: 8px;
}

.form-card {
  border-radius: 12px;
}

.unit {
  margin-left: 8px;
  color: #999;
  font-size: 13px;
}
</style>
