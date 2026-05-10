<template>
  <div class="history-page">
    <header class="page-header animate-in">
      <h1 class="page-title">运动历史</h1>
      <p class="page-subtitle">回顾你的训练轨迹</p>
    </header>

    <div class="stats-row">
      <div v-for="(s, i) in statCards" :key="s.title" class="animate-in" :style="{ animationDelay: (i * 0.08 + 0.1) + 's' }">
        <StatCard
          :title="s.title"
          :value="s.value"
          :subtitle="s.subtitle"
          :icon="s.icon"
          :color="s.color"
        />
      </div>
    </div>

    <section class="records-section animate-in" style="animation-delay: 0.35s">
      <div class="section-header">
        <h2 class="section-title">运动记录</h2>
        <el-select v-model="filterType" placeholder="全部类型" clearable style="width: 140px" @change="loadRecords">
          <el-option label="有氧运动" value="有氧运动" />
          <el-option label="力量训练" value="力量训练" />
          <el-option label="功能性训练" value="功能性训练" />
        </el-select>
      </div>

      <div v-if="records.length === 0" class="empty-state">
        <div class="empty-icon">◈</div>
        <p class="empty-text">还没有运动记录</p>
        <p class="empty-sub">开始你的健身之旅</p>
        <button class="empty-cta" @click="$router.push('/quick-check')">开始打卡</button>
      </div>

      <div v-else>
        <ExerciseCard
          v-for="record in records"
          :key="record.id"
          :record="record"
          :show-delete="true"
          @delete="handleDelete"
          @edit="handleEdit"
        />
        <div class="pagination-wrap">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="loadRecords"
          />
        </div>
      </div>
    </section>

    <el-dialog v-model="editDialogVisible" title="修改运动记录" width="480px" :close-on-click-modal="false">
      <el-form :model="editForm" label-width="90px" ref="editFormRef" :rules="editRules">
        <el-form-item label="运动类型" prop="type">
          <el-select v-model="editForm.type" placeholder="选择类型" style="width: 100%">
            <el-option label="有氧运动" value="有氧运动" />
            <el-option label="力量训练" value="力量训练" />
            <el-option label="功能性训练" value="功能性训练" />
          </el-select>
        </el-form-item>
        <el-form-item label="运动日期" prop="date">
          <el-date-picker
            v-model="editForm.date"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="运动时长" prop="duration">
          <el-input-number v-model="editForm.duration" :min="1" style="width: 100%" />
          <span class="unit-label">分钟</span>
        </el-form-item>
        <el-form-item label="消耗卡路里">
          <el-input-number v-model="editForm.calories" :min="0" style="width: 100%" />
          <span class="unit-label">kcal</span>
        </el-form-item>
        <el-form-item label="平均心率">
          <el-input-number v-model="editForm.avgHeartRate" :min="0" style="width: 100%" />
          <span class="unit-label">bpm</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editForm.note" type="textarea" :rows="3" placeholder="记录训练感受..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate" :loading="editLoading">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getRecords, getTotalStats, deleteRecord, updateRecord } from '../api/exercise'
import { useExerciseStore } from '../stores/exercise'
import { ElMessage, ElMessageBox } from 'element-plus'
import StatCard from '../components/StatCard.vue'
import ExerciseCard from '../components/ExerciseCard.vue'

const store = useExerciseStore()
const totalStats = ref({ count: 0, totalDuration: 0, totalCalories: 0 })
const records = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterType = ref('')

const editDialogVisible = ref(false)
const editLoading = ref(false)
const editFormRef = ref(null)
const editingId = ref(null)
const editForm = reactive({
  type: '',
  date: '',
  duration: null,
  calories: null,
  avgHeartRate: null,
  note: ''
})

const editRules = {
  type: [{ required: true, message: '请选择运动类型', trigger: 'change' }],
  date: [{ required: true, message: '请选择运动日期', trigger: 'change' }],
  duration: [{ required: true, message: '请输入运动时长', trigger: 'blur' }]
}

const statCards = computed(() => [
  {
    title: '总运动次数',
    value: totalStats.value.count,
    subtitle: '累计打卡',
    icon: 'TrendCharts',
    color: '#f59e0b'
  },
  {
    title: '总运动时长',
    value: totalStats.value.totalDuration,
    subtitle: '分钟',
    icon: 'Timer',
    color: '#10b981'
  },
  {
    title: '总消耗卡路里',
    value: totalStats.value.totalCalories,
    subtitle: 'kcal',
    icon: 'Flame',
    color: '#ef4444'
  }
])

async function loadStats() {
  try {
    const res = await getTotalStats(store.userId)
    totalStats.value = res.data
  } catch (e) {
    console.error(e)
  }
}

async function loadRecords() {
  try {
    const res = await getRecords({
      userId: store.userId,
      page: page.value,
      size: pageSize.value,
      type: filterType.value || undefined
    })
    records.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除这条记录吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteRecord(id)
    ElMessage.success('删除成功')
    loadRecords()
    loadStats()
  } catch (e) {
    if (e !== 'cancel') console.error(e)
  }
}

function handleEdit(record) {
  editingId.value = record.id
  editForm.type = record.type
  editForm.date = record.date
  editForm.duration = record.duration
  editForm.calories = record.calories
  editForm.avgHeartRate = record.avgHeartRate
  editForm.note = record.note || ''
  editDialogVisible.value = true
}

async function handleUpdate() {
  try {
    await editFormRef.value.validate()
  } catch {
    return
  }

  editLoading.value = true
  try {
    await updateRecord(editingId.value, {
      userId: store.userId,
      ...editForm
    })
    ElMessage.success('修改成功')
    editDialogVisible.value = false
    loadRecords()
    loadStats()
  } catch (e) {
    console.error(e)
  } finally {
    editLoading.value = false
  }
}

onMounted(() => {
  loadStats()
  loadRecords()
})
</script>

<style scoped>
.history-page {
  max-width: 1000px;
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

.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
  margin-bottom: 28px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.3px;
}

.empty-state {
  text-align: center;
  padding: 60px 24px;
  background: var(--bg-card);
  border: 1px dashed var(--border-medium);
  border-radius: var(--radius-lg);
}

.empty-icon {
  font-size: 40px;
  color: var(--text-muted);
  margin-bottom: 16px;
  opacity: 0.4;
}

.empty-text {
  color: var(--text-secondary);
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 4px;
}

.empty-sub {
  color: var(--text-muted);
  font-size: 13px;
  margin-bottom: 20px;
}

.empty-cta {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 24px;
  background: var(--accent);
  color: #000;
  border: none;
  border-radius: var(--radius-full);
  font-family: var(--font-body);
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.empty-cta:hover {
  background: var(--accent-bright);
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.unit-label {
  margin-left: 8px;
  color: var(--text-muted);
  font-size: 13px;
}
</style>
