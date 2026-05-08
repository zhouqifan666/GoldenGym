<template>
  <div class="history-page">
    <h1 class="page-title">运动历史</h1>

    <div class="stats-row">
      <StatCard
        title="总运动次数"
        :value="totalStats.count"
        subtitle="累计打卡"
        icon="TrendCharts"
        color="#F4672A"
      />
      <StatCard
        title="总运动时长"
        :value="totalStats.totalDuration"
        subtitle="分钟"
        icon="Timer"
        color="#67C23A"
      />
      <StatCard
        title="总消耗卡路里"
        :value="totalStats.totalCalories"
        subtitle="kcal"
        icon="Flame"
        color="#67C23A"
      />
    </div>

    <div class="section">
      <div class="section-header">
        <h2>运动记录</h2>
        <el-select v-model="filterType" placeholder="全部类型" clearable style="width: 150px" @change="loadRecords">
          <el-option label="有氧运动" value="有氧运动" />
          <el-option label="力量训练" value="力量训练" />
          <el-option label="功能性训练" value="功能性训练" />
        </el-select>
      </div>

      <div v-if="records.length === 0" class="empty-state">
        <el-icon :size="64" color="#ddd"><TrendCharts /></el-icon>
        <p class="empty-text">还没有运动记录</p>
        <el-button type="warning" @click="$router.push('/quick-check')">开始第一次打卡</el-button>
      </div>

      <div v-else>
        <ExerciseCard
          v-for="record in records"
          :key="record.id"
          :record="record"
          :show-delete="true"
          @delete="handleDelete"
        />
        <div class="pagination">
          <el-pagination
            v-model:current-page="page"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="loadRecords"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRecords, getTotalStats, deleteRecord } from '../api/exercise'
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

onMounted(() => {
  loadStats()
  loadRecords()
})
</script>

<style scoped>
.history-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 24px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h2 {
  font-size: 18px;
  font-weight: 600;
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

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
