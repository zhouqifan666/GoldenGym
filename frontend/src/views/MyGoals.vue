<template>
  <div class="goals-page">
    <header class="page-header animate-in">
      <div>
        <h1 class="page-title">我的目标</h1>
        <p class="page-subtitle">设定目标，坚持打卡</p>
      </div>
      <button class="create-btn" @click="showDialog = true">
        <span>+</span> 创建目标
      </button>
    </header>

    <div v-if="goals.length === 0" class="empty-state animate-in" style="animation-delay: 0.15s">
      <div class="empty-icon">◎</div>
      <p class="empty-text">还没有设置目标</p>
      <p class="empty-sub">设定你的第一个健身目标</p>
      <button class="empty-cta" @click="showDialog = true">创建目标</button>
    </div>

    <div v-else class="goals-grid">
      <div v-for="(goal, i) in goals" :key="goal.id" class="animate-in" :style="{ animationDelay: (i * 0.06 + 0.1) + 's' }">
        <GoalCard
          :goal="goal"
          @toggle="handleToggle"
          @delete="handleDelete"
        />
      </div>
    </div>

    <el-dialog v-model="showDialog" title="创建目标" width="460px" :close-on-click-modal="false">
      <el-form :model="goalForm" label-width="90px" ref="goalFormRef" :rules="goalRules">
        <el-form-item label="目标周期" prop="period">
          <el-select v-model="goalForm.period" placeholder="选择目标周期" style="width: 100%">
            <el-option label="每日目标" value="每日" />
            <el-option label="每周目标" value="每周" />
            <el-option label="每月目标" value="每月" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标天数">
          <el-input-number v-model="goalForm.targetDays" :min="1" placeholder="例如：5天" style="width: 100%" />
        </el-form-item>
        <el-form-item label="目标时长">
          <el-input-number v-model="goalForm.targetDuration" :min="1" placeholder="例如：150分钟" style="width: 100%" />
          <span class="unit-label">分钟</span>
        </el-form-item>
        <el-form-item label="目标卡路里">
          <el-input-number v-model="goalForm.targetCalories" :min="1" placeholder="例如：1000卡" style="width: 100%" />
          <span class="unit-label">kcal</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="dialog-submit" @click="handleCreateGoal" :disabled="creating">
          <span v-if="creating" class="btn-spinner"></span>
          <span v-else>创建目标</span>
        </button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { createGoal, listGoals, toggleComplete, deleteGoal } from '../api/goal'
import { useExerciseStore } from '../stores/exercise'
import { ElMessage, ElMessageBox } from 'element-plus'
import GoalCard from '../components/GoalCard.vue'

const store = useExerciseStore()
const goals = ref([])
const showDialog = ref(false)
const creating = ref(false)
const goalFormRef = ref(null)

const goalForm = reactive({
  period: '',
  targetDays: null,
  targetDuration: null,
  targetCalories: null
})

const goalRules = {
  period: [{ required: true, message: '请选择目标周期', trigger: 'change' }]
}

async function loadGoals() {
  try {
    const res = await listGoals(store.userId)
    goals.value = res.data
  } catch (e) {
    console.error(e)
  }
}

async function handleCreateGoal() {
  try {
    await goalFormRef.value.validate()
  } catch {
    return
  }
  creating.value = true
  try {
    await createGoal({ userId: store.userId, ...goalForm })
    ElMessage.success('目标创建成功')
    showDialog.value = false
    goalForm.period = ''
    goalForm.targetDays = null
    goalForm.targetDuration = null
    goalForm.targetCalories = null
    loadGoals()
  } catch (e) {
    console.error(e)
  } finally {
    creating.value = false
  }
}

async function handleToggle(id) {
  try {
    await toggleComplete(id)
    loadGoals()
  } catch (e) {
    console.error(e)
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定删除这个目标吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteGoal(id)
    ElMessage.success('删除成功')
    loadGoals()
  } catch (e) {
    if (e !== 'cancel') console.error(e)
  }
}

onMounted(loadGoals)
</script>

<style scoped>
.goals-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 28px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.create-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 22px;
  background: var(--accent);
  color: #000;
  border: none;
  border-radius: var(--radius-full);
  font-family: var(--font-body);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px var(--accent-glow-strong);
}

.create-btn:hover {
  background: var(--accent-bright);
  transform: translateY(-1px);
}

.create-btn span {
  font-size: 18px;
  line-height: 1;
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

.goals-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 14px;
}

.unit-label {
  margin-left: 8px;
  color: var(--text-muted);
  font-size: 13px;
}

.dialog-submit {
  width: 100%;
  padding: 12px 24px;
  background: var(--accent);
  color: #000;
  border: none;
  border-radius: var(--radius-sm);
  font-family: var(--font-body);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s;
}

.dialog-submit:hover:not(:disabled) {
  background: var(--accent-bright);
}

.dialog-submit:disabled {
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
