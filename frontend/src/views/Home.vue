<template>
  <div class="home-page">
    <header class="hero animate-in">
      <div class="hero-content">
        <div class="hero-badge">GOLDEN GYM</div>
        <h1 class="hero-title">记录每一次运动<br /><span class="title-accent">见证你的蜕变</span></h1>
        <p class="hero-desc">坚持打卡，用数据驱动你的健身旅程</p>
      </div>
      <button class="hero-cta" @click="$router.push('/quick-check')">
        <span class="cta-icon">⚡</span>
        快速打卡
      </button>
    </header>

    <div class="stats-row">
      <div v-for="(s, i) in statCards" :key="s.title" class="animate-in" :class="{ 'clickable': s.route }" :style="{ animationDelay: (i * 0.08) + 's' }" @click="s.route && $router.push(s.route)">
        <StatCard
          :title="s.title"
          :value="s.value"
          :subtitle="s.subtitle"
          :icon="s.icon"
          :color="s.color"
        />
      </div>
    </div>

    <section class="recent-section animate-in" style="animation-delay: 0.3s">
      <div class="section-header">
        <h2 class="section-title">最近运动</h2>
        <router-link to="/history" class="section-link">查看全部 →</router-link>
      </div>

      <div v-if="records.length === 0" class="empty-state">
        <div class="empty-icon">◆</div>
        <p class="empty-text">还没有运动记录</p>
        <p class="empty-sub">开始你的第一次打卡吧</p>
        <button class="empty-cta" @click="$router.push('/quick-check')">开始打卡</button>
      </div>

      <div v-else>
        <ExerciseCard v-for="record in records" :key="record.id" :record="record" />
        <div ref="sentinelRef" class="load-sentinel">
          <span v-if="loadingMore" class="loading-spinner"></span>
          <span v-else-if="!hasMore" class="load-end">已加载全部记录</span>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { getWeekStats, getMonthStats, getRecords } from '../api/exercise'
import { listGoals } from '../api/goal'
import { useExerciseStore } from '../stores/exercise'
import StatCard from '../components/StatCard.vue'
import ExerciseCard from '../components/ExerciseCard.vue'

const store = useExerciseStore()
const weekStats = ref({ count: 0, days: 0, totalDuration: 0, totalCalories: 0 })
const monthStats = ref({ count: 0, days: 0, totalDuration: 0, totalCalories: 0 })
const activeGoals = ref(0)
const records = ref([])

const page = ref(1)
const pageSize = 10
const hasMore = ref(false)
const loadingMore = ref(false)
const sentinelRef = ref(null)
let observer = null

const statCards = computed(() => [
  {
    title: '本周运动',
    value: weekStats.value.days + '天',
    subtitle: `${weekStats.value.count}次 | ${weekStats.value.totalDuration}分钟 | ${weekStats.value.totalCalories}kcal`,
    icon: 'Calendar',
    color: '#f59e0b'
  },
  {
    title: '本月运动',
    value: monthStats.value.days + '天',
    subtitle: `${monthStats.value.count}次 | ${monthStats.value.totalDuration}分钟 | ${monthStats.value.totalCalories}kcal`,
    icon: 'TrendCharts',
    color: '#10b981'
  },
  {
    title: '活跃目标',
    value: activeGoals.value,
    subtitle: '未完成目标',
    icon: 'Aim',
    color: '#6366f1',
    route: '/goals'
  }
])

async function loadMore() {
  if (!hasMore.value || loadingMore.value) return
  loadingMore.value = true
  try {
    page.value++
    const res = await getRecords({ userId: store.userId, page: page.value, size: pageSize })
    records.value.push(...res.data.records)
    hasMore.value = records.value.length < res.data.total
  } catch (e) {
    page.value--
    console.error(e)
  } finally {
    loadingMore.value = false
  }
}

function setupObserver() {
  if (observer) observer.disconnect()
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting && hasMore.value && !loadingMore.value) {
      loadMore()
    }
  }, { threshold: 0 })
  nextTick(() => {
    if (sentinelRef.value) observer.observe(sentinelRef.value)
  })
}

async function loadData() {
  try {
    const [weekRes, monthRes, recordsRes, goalsRes] = await Promise.all([
      getWeekStats(store.userId),
      getMonthStats(store.userId),
      getRecords({ userId: store.userId, page: 1, size: pageSize }),
      listGoals(store.userId)
    ])
    weekStats.value = weekRes.data
    monthStats.value = monthRes.data
    records.value = recordsRes.data.records
    hasMore.value = records.value.length < recordsRes.data.total
    activeGoals.value = goalsRes.data.filter(g => !g.completed).length
    setupObserver()
  } catch (e) {
    console.error(e)
  }
}

onMounted(loadData)

onUnmounted(() => {
  if (observer) observer.disconnect()
})
</script>

<style scoped>
.home-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 28px;
}

.hero {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32px 36px;
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-xl);
  margin-bottom: 24px;
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: -60px;
  right: -60px;
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, var(--accent-glow) 0%, transparent 70%);
  pointer-events: none;
}

.hero::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--accent-glow-strong), transparent);
}

.hero-content {
  position: relative;
  z-index: 1;
}

.hero-badge {
  display: inline-block;
  padding: 4px 12px;
  background: var(--accent-glow);
  color: var(--accent);
  border-radius: var(--radius-full);
  font-family: var(--font-display);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 2px;
  margin-bottom: 14px;
}

.hero-title {
  font-family: var(--font-display);
  font-size: 28px;
  font-weight: 800;
  color: var(--text-primary);
  line-height: 1.3;
  letter-spacing: -0.5px;
}

.title-accent {
  background: linear-gradient(135deg, var(--accent), var(--accent-bright));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-desc {
  font-size: 14px;
  color: var(--text-muted);
  margin-top: 10px;
}

.hero-cta {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
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
  white-space: nowrap;
}

.hero-cta:hover {
  background: var(--accent-bright);
  transform: translateY(-1px);
  box-shadow: 0 6px 24px var(--accent-glow-strong);
}

.cta-icon {
  font-size: 16px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
  margin-bottom: 28px;
}

.clickable {
  cursor: pointer;
}

.clickable :deep(.stat-card) {
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s ease;
}

.clickable :deep(.stat-card:hover) {
  transform: translateY(-2px);
  box-shadow: 0 8px 32px rgba(99, 102, 241, 0.15);
}

.recent-section {
  margin-bottom: 40px;
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

.section-link {
  font-size: 13px;
  color: var(--accent);
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.2s;
}

.section-link:hover {
  opacity: 0.8;
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

.load-sentinel {
  text-align: center;
  padding: 24px;
}

.loading-spinner {
  display: inline-block;
  width: 24px;
  height: 24px;
  border: 2px solid var(--border-medium);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

.load-end {
  font-size: 13px;
  color: var(--text-muted);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
