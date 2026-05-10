<template>
  <div class="calendar-page">
    <header class="page-header animate-in">
      <h1 class="page-title">运动日历</h1>
      <p class="page-subtitle">按日期查看你的训练记录</p>
    </header>

    <div class="calendar-layout">
      <div class="calendar-main animate-in" style="animation-delay: 0.1s">
        <div class="calendar-nav">
          <button class="nav-arrow" @click="prevMonth">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="15 18 9 12 15 6" />
            </svg>
          </button>
          <span class="calendar-month">{{ currentYear }}年{{ currentMonth }}月</span>
          <button class="nav-arrow" @click="nextMonth">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="9 18 15 12 9 6" />
            </svg>
          </button>
          <button class="today-btn" @click="goToday">今天</button>
        </div>

        <div class="calendar-grid">
          <div class="weekday" v-for="d in weekdays" :key="d">{{ d }}</div>
          <div
            v-for="(day, index) in calendarDays"
            :key="index"
            class="day-cell"
            :class="{
              'other-month': day.otherMonth,
              'today': day.isToday,
              'has-record': day.hasRecord,
              'selected': day.dateStr === selectedDate,
              'weekend': day.isWeekend
            }"
            @click="selectDate(day)"
          >
            <span class="day-number">{{ day.day }}</span>
            <span v-if="day.hasRecord" class="record-dot"></span>
          </div>
        </div>

        <div class="legend">
          <div class="legend-item">
            <span class="legend-swatch today-swatch"></span>
            <span>今天</span>
          </div>
          <div class="legend-item">
            <span class="legend-swatch record-swatch"></span>
            <span>有运动记录</span>
          </div>
          <div class="legend-item">
            <span class="legend-swatch selected-swatch"></span>
            <span>选中日期</span>
          </div>
        </div>
      </div>

      <div class="detail-panel animate-in" style="animation-delay: 0.2s">
        <div class="detail-header">
          <span class="detail-icon">▦</span>
          <span class="detail-date">{{ selectedDate || '选择日期' }}</span>
        </div>

        <div v-if="!selectedDate" class="detail-empty">
          <div class="detail-empty-icon">▦</div>
          <p>点击日历选择日期</p>
        </div>

        <div v-else-if="dayRecords.length === 0" class="detail-empty">
          <p>该日期暂无运动记录</p>
        </div>

        <div v-else class="detail-list">
          <div v-for="record in dayRecords" :key="record.id" class="detail-record">
            <div class="detail-badge" :class="getTypeClass(record.type)">{{ record.type }}</div>
            <div class="detail-metrics">
              <span class="detail-metric">
                <span class="dm-icon">◷</span>
                {{ record.duration }}分钟
              </span>
              <span class="detail-metric" v-if="record.calories">
                <span class="dm-icon">◈</span>
                {{ record.calories }}kcal
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getCalendarDates, getRecordsByDate } from '../api/exercise'
import { useExerciseStore } from '../stores/exercise'

const store = useExerciseStore()
const weekdays = ['一', '二', '三', '四', '五', '六', '日']

const today = new Date()
const currentYear = ref(today.getFullYear())
const currentMonth = ref(today.getMonth() + 1)
const selectedDate = ref('')
const recordDates = ref([])
const dayRecords = ref([])

function getTypeClass(type) {
  const classes = { '有氧运动': 'type-green', '力量训练': 'type-amber', '功能性训练': 'type-red' }
  return classes[type] || 'type-default'
}

const calendarDays = computed(() => {
  const year = currentYear.value
  const month = currentMonth.value
  const firstDay = new Date(year, month - 1, 1)
  const lastDay = new Date(year, month, 0)

  let startWeekday = firstDay.getDay()
  if (startWeekday === 0) startWeekday = 7

  const days = []

  // 上月日期
  const prevMonthLast = new Date(year, month - 1, 0)
  const prevMonth = month === 1 ? 12 : month - 1
  const prevYear = month === 1 ? year - 1 : year
  for (let i = startWeekday - 1; i > 0; i--) {
    const d = prevMonthLast.getDate() - i + 1
    const dateStr = `${prevYear}-${String(prevMonth).padStart(2, '0')}-${String(d).padStart(2, '0')}`
    days.push({ day: d, otherMonth: true, isToday: false, hasRecord: recordDates.value.includes(dateStr), isWeekend: false, dateStr })
  }

  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`

  // 本月日期
  for (let d = 1; d <= lastDay.getDate(); d++) {
    const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(d).padStart(2, '0')}`
    const dayOfWeek = new Date(year, month - 1, d).getDay()
    days.push({
      day: d,
      otherMonth: false,
      isToday: dateStr === todayStr,
      hasRecord: recordDates.value.includes(dateStr),
      isWeekend: dayOfWeek === 0 || dayOfWeek === 6,
      dateStr
    })
  }

  // 下月日期
  const nextMonth = month === 12 ? 1 : month + 1
  const nextYear = month === 12 ? year + 1 : year
  const remaining = 42 - days.length
  for (let d = 1; d <= remaining; d++) {
    const dateStr = `${nextYear}-${String(nextMonth).padStart(2, '0')}-${String(d).padStart(2, '0')}`
    days.push({ day: d, otherMonth: true, isToday: false, hasRecord: recordDates.value.includes(dateStr), isWeekend: false, dateStr })
  }

  return days
})

async function loadRecordDates() {
  try {
    const year = currentYear.value
    const month = currentMonth.value
    const prevMonth = month === 1 ? 12 : month - 1
    const prevYear = month === 1 ? year - 1 : year
    const nextMonth = month === 12 ? 1 : month + 1
    const nextYear = month === 12 ? year + 1 : year

    const [prevRes, currRes, nextRes] = await Promise.all([
      getCalendarDates(prevYear, prevMonth, store.userId),
      getCalendarDates(year, month, store.userId),
      getCalendarDates(nextYear, nextMonth, store.userId)
    ])
    recordDates.value = [...prevRes.data, ...currRes.data, ...nextRes.data]
  } catch (e) {
    console.error(e)
  }
}

async function selectDate(day) {
  if (!day.dateStr) return
  selectedDate.value = day.dateStr
  try {
    const res = await getRecordsByDate(day.dateStr, store.userId)
    dayRecords.value = res.data
  } catch (e) {
    console.error(e)
  }
}

function prevMonth() {
  if (currentMonth.value === 1) {
    currentMonth.value = 12
    currentYear.value--
  } else {
    currentMonth.value--
  }
  selectedDate.value = ''
  dayRecords.value = []
  loadRecordDates()
}

function nextMonth() {
  if (currentMonth.value === 12) {
    currentMonth.value = 1
    currentYear.value++
  } else {
    currentMonth.value++
  }
  selectedDate.value = ''
  dayRecords.value = []
  loadRecordDates()
}

async function goToday() {
  currentYear.value = today.getFullYear()
  currentMonth.value = today.getMonth() + 1
  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`
  selectedDate.value = todayStr
  await loadRecordDates()
  try {
    const res = await getRecordsByDate(todayStr, store.userId)
    dayRecords.value = res.data
  } catch (e) {
    console.error(e)
  }
}

onMounted(loadRecordDates)
</script>

<style scoped>
.calendar-page {
  max-width: 1100px;
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

.calendar-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 16px;
}

.calendar-main {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
}

.calendar-nav {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.nav-arrow {
  width: 34px;
  height: 34px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-subtle);
  background: var(--bg-card);
  color: var(--text-secondary);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.nav-arrow:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-glow);
}

.calendar-month {
  font-family: var(--font-display);
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
  min-width: 100px;
  text-align: center;
  letter-spacing: -0.3px;
}

.today-btn {
  margin-left: auto;
  padding: 6px 16px;
  border-radius: var(--radius-full);
  border: 1px solid var(--border-subtle);
  background: var(--bg-card);
  color: var(--text-secondary);
  font-family: var(--font-body);
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.today-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-glow);
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.weekday {
  text-align: center;
  font-size: 11px;
  font-weight: 600;
  color: var(--text-muted);
  padding: 8px 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.day-cell {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
  gap: 3px;
}

.day-cell:hover {
  background: var(--bg-card-hover);
}

.day-number {
  font-family: var(--font-display);
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
}

.day-cell.other-month .day-number {
  color: var(--text-muted);
  opacity: 0.4;
}

.day-cell.other-month:not(.has-record) {
  background: transparent;
}

.day-cell.today {
  background: var(--accent-glow);
  box-shadow: 0 0 0 2px var(--accent-dim), 0 2px 8px var(--accent-glow-strong);
}

.day-cell.today .day-number {
  color: var(--accent);
  font-weight: 700;
}

.day-cell.has-record {
  background: rgba(16, 185, 129, 0.06);
}

.day-cell.selected {
  background: var(--info-glow);
  border: 1px solid var(--info);
  box-shadow: 0 0 0 1px rgba(99, 102, 241, 0.3), 0 2px 8px rgba(99, 102, 241, 0.15);
}

.day-cell.selected .day-number {
  color: var(--info);
  font-weight: 700;
}

/* Priority: today > selected > has-record */
.day-cell.today.has-record {
  background: var(--accent-glow);
}

.day-cell.today.selected {
  background: var(--accent-glow);
  border: 1px solid var(--accent-dim);
  box-shadow: 0 0 0 2px var(--accent-dim), 0 2px 8px var(--accent-glow-strong);
}

.day-cell.today.selected .day-number {
  color: var(--accent);
}

.day-cell.selected.has-record {
  background: var(--info-glow);
}

.day-cell.weekend .day-number {
  color: var(--info);
}

.record-dot {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: var(--success);
}

.legend {
  display: flex;
  gap: 20px;
  margin-top: 20px;
  justify-content: center;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--text-muted);
}

.legend-swatch {
  width: 14px;
  height: 14px;
  border-radius: 4px;
}

.today-swatch {
  background: var(--accent-glow);
  border: 1px solid var(--accent-dim);
}

.record-swatch {
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.selected-swatch {
  background: var(--info-glow);
  border: 1px solid var(--info);
}

.detail-panel {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
  height: fit-content;
  position: sticky;
  top: 80px;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border-subtle);
}

.detail-icon {
  font-size: 18px;
  color: var(--accent);
}

.detail-date {
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 700;
  color: var(--text-primary);
}

.detail-empty {
  text-align: center;
  padding: 40px 0;
  color: var(--text-muted);
  font-size: 13px;
}

.detail-empty-icon {
  font-size: 32px;
  opacity: 0.3;
  margin-bottom: 12px;
}

.detail-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-record {
  padding: 14px;
  background: var(--bg-secondary);
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-subtle);
}

.detail-badge {
  display: inline-flex;
  padding: 3px 10px;
  border-radius: var(--radius-full);
  font-size: 11px;
  font-weight: 600;
  margin-bottom: 8px;
}

.type-green {
  background: var(--success-glow);
  color: var(--success);
}

.type-amber {
  background: var(--accent-glow);
  color: var(--accent);
}

.type-red {
  background: var(--danger-glow);
  color: var(--danger);
}

.type-default {
  background: var(--info-glow);
  color: var(--info);
}

.detail-metrics {
  display: flex;
  gap: 16px;
}

.detail-metric {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-secondary);
}

.dm-icon {
  font-size: 10px;
  color: var(--text-muted);
}
</style>
