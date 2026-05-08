<template>
  <div class="goals-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">我的目标</h1>
        <p class="page-subtitle">设定目标，坚持打卡</p>
      </div>
      <el-button type="warning" @click="showDialog = true">
        <el-icon><Plus /></el-icon>
        创建目标
      </el-button>
    </div>

    <div v-if="goals.length === 0" class="empty-state">
      <el-icon :size="64" color="#ddd"><Aim /></el-icon>
      <p class="empty-text">还没有设置目标</p>
      <el-button type="warning" @click="showDialog = true">创建第一个目标</el-button>
    </div>

    <div v-else class="goals-grid">
      <GoalCard
        v-for="goal in goals"
        :key="goal.id"
        :goal="goal"
        @toggle="handleToggle"
        @delete="handleDelete"
      />
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
          <span class="unit">分钟</span>
        </el-form-item>
        <el-form-item label="目标卡路里">
          <el-input-number v-model="goalForm.targetCalories" :min="1" placeholder="例如：1000卡" style="width: 100%" />
          <span class="unit">kcal</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="warning" @click="handleCreateGoal" :loading="creating" style="width: 100%">
          创建目标
        </el-button>
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
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
}

.page-subtitle {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  background: #fff;
  border-radius: 12px;
}

.empty-text {
  color: #999;
  margin: 16px 0;
}

.goals-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.unit {
  margin-left: 8px;
  color: #999;
  font-size: 13px;
}
</style>
