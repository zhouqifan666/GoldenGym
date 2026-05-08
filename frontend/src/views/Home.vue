<template>
  <div class="home-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">健身打卡助手</h1>
        <p class="page-subtitle">记录每一次运动，见证你的蜕变</p>
      </div>
      <el-button type="warning" @click="$router.push('/quick-check')">
        <el-icon><Plus /></el-icon>
        快速打卡
      </el-button>
    </div>

    <div class="stats-row">
      <StatCard
        title="本周运动"
        :value="weekStats.count"
        :subtitle="`${weekStats.totalDuration}分钟 | ${weekStats.totalCalories}kcal`"
        icon="Calendar"
        color="#F4672A"
      />
      <StatCard
        title="本月运动"
        :value="monthStats.count"
        :subtitle="`${monthStats.totalDuration}分钟 | ${monthStats.totalCalories}kcal`"
        icon="TrendCharts"
        color="#67C23A"
      />
      <StatCard
        title="活跃目标"
        :value="activeGoals"
        subtitle="未完成目标"
        icon="Aim"
        color="#67C23A"
      />
    </div>

    <div class="section">
      <div class="section-header">
        <h2>最近运动</h2>
        <el-button link type="primary" @click="$router.push('/history')">查看全部</el-button>
      </div>

      <div v-if="records.length === 0" class="empty-state">
        <el-icon :size="64" color="#ddd"><TrendCharts /></el-icon>
        <p class="empty-text">还没有运动记录</p>
        <el-button type="warning" @click="$router.push('/quick-check')">开始第一次打卡</el-button>
      </div>

      <div v-else>
        <ExerciseCard v-for="record in records" :key="record.id" :record="record" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getWeekStats, getMonthStats, getRecords } from '../api/exercise'
import { listGoals } from '../api/goal'
import { useExerciseStore } from '../stores/exercise'
import StatCard from '../components/StatCard.vue'
import ExerciseCard from '../components/ExerciseCard.vue'

const store = useExerciseStore()
const weekStats = ref({ count: 0, totalDuration: 0, totalCalories: 0 })
const monthStats = ref({ count: 0, totalDuration: 0, totalCalories: 0 })
const activeGoals = ref(0)
const records = ref([])

async function loadData() {
  try {
    const [weekRes, monthRes, recordsRes, goalsRes] = await Promise.all([
      getWeekStats(store.userId),
      getMonthStats(store.userId),
      getRecords({ userId: store.userId, page: 1, size: 5 }),
      listGoals(store.userId)
    ])
    weekStats.value = weekRes.data
    monthStats.value = monthRes.data
    records.value = recordsRes.data.records
    activeGoals.value = goalsRes.data.filter(g => !g.completed).length
  } catch (e) {
    console.error(e)
  }
}

onMounted(loadData)
</script>

<style scoped>
.home-page {
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
  color: #333;
}

.page-subtitle {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
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
</style>
