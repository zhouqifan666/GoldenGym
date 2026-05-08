<template>
  <div class="calendar-page">
    <h1 class="page-title">运动日历</h1>

    <div class="calendar-layout">
      <div class="calendar-main">
        <div class="calendar-header">
          <el-button circle @click="prevMonth" type="warning" plain>
            <el-icon><ArrowLeft /></el-icon>
          </el-button>
          <span class="calendar-title">{{ currentYear }}年{{ currentMonth }}月</span>
          <el-button circle @click="nextMonth" type="warning" plain>
            <el-icon><ArrowRight /></el-icon>
          </el-button>
          <el-button type="warning" size="small" @click="goToday" style="margin-left: 12px">今天</el-button>
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
          </div>
        </div>

        <div class="legend">
          <div class="legend-item">
            <span class="legend-box today-legend"></span>
            <span>今天</span>
          </div>
          <div class="legend-item">
            <span class="legend-box record-legend"></span>
            <span>有运动记录</span>
          </div>
          <div class="legend-item">
            <span class="legend-box selected-legend"></span>
            <span>选中日期</span>
          </div>
        </div>
      </div>

      <div class="detail-panel">
        <div class="detail-header">
          <el-icon><Calendar /></el-icon>
          <span>{{ selectedDate || '选择日期' }}</span>
        </div>

        <div v-if="!selectedDate" class="detail-empty">
          <el-icon :size="48" color="#ddd"><Calendar /></el-icon>
          <p>点击日历选择日期</p>
        </div>

        <div v-else-if="dayRecords.length === 0" class="detail-empty">
          <p>该日期暂无运动记录</p>
        </div>

        <div v-else class="detail-list">
          <div v-for="record in dayRecords" :key="record.id" class="detail-record">
            <el-tag :type="getTagType(record.type)" effect="dark" size="small">{{ record.type }}</el-tag>
            <div class="record-info">
              <span>{{ record.duration }}分钟</span>
              <span v-if="record.calories">{{ record.calories }}kcal</span>
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

function getTagType(type) {
  const types = { '有氧运动': 'success', '力量训练': 'warning', '功能性训练': 'danger' }
  return types[type] || 'info'
}

const calendarDays = computed(() => {
  const year = currentYear.value
  const month = currentMonth.value
  const firstDay = new Date(year, month - 1, 1)
  const lastDay = new Date(year, month, 0)

  let startWeekday = firstDay.getDay()
  if (startWeekday === 0) startWeekday = 7

  const days = []

  // Previous month days
  const prevMonthLast = new Date(year, month - 1, 0)
  for (let i = startWeekday - 1; i > 0; i--) {
    const d = prevMonthLast.getDate() - i + 1
    days.push({
      day: d,
      otherMonth: true,
      isToday: false,
      hasRecord: false,
      isWeekend: false,
      dateStr: ''
    })
  }

  // Current month days
  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`

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

  // Next month days
  const remaining = 42 - days.length
  for (let d = 1; d <= remaining; d++) {
    days.push({
      day: d,
      otherMonth: true,
      isToday: false,
      hasRecord: false,
      isWeekend: false,
      dateStr: ''
    })
  }

  return days
})

async function loadRecordDates() {
  try {
    const res = await getCalendarDates(currentYear.value, currentMonth.value, store.userId)
    recordDates.value = res.data
  } catch (e) {
    console.error(e)
  }
}

async function selectDate(day) {
  if (day.otherMonth || !day.dateStr) return
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

function goToday() {
  currentYear.value = today.getFullYear()
  currentMonth.value = today.getMonth() + 1
  selectedDate.value = ''
  dayRecords.value = []
  loadRecordDates()
}

onMounted(loadRecordDates)
</script>

<style scoped>
.calendar-page {
  max-width: 1100px;
  margin: 0 auto;
  padding: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 24px;
}

.calendar-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.calendar-main {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.calendar-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.calendar-title {
  font-size: 18px;
  font-weight: 600;
  min-width: 120px;
  text-align: center;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.weekday {
  text-align: center;
  font-size: 13px;
  color: #999;
  padding: 8px 0;
}

.day-cell {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.15s;
  position: relative;
}

.day-cell:hover {
  background: #f5f5f5;
}

.day-number {
  font-size: 14px;
}

.day-cell.other-month .day-number {
  color: #ccc;
}

.day-cell.today {
  border: 2px solid #F4672A;
}

.day-cell.today .day-number {
  color: #F4672A;
  font-weight: 600;
}

.day-cell.has-record {
  background: #e8f5e9;
}

.day-cell.selected {
  border: 2px solid #F4672A;
  background: #fff3e0;
}

.day-cell.weekend .day-number {
  color: #409EFF;
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
  font-size: 13px;
  color: #999;
}

.legend-box {
  width: 16px;
  height: 16px;
  border-radius: 4px;
}

.today-legend {
  border: 2px solid #F4672A;
}

.record-legend {
  background: #e8f5e9;
}

.selected-legend {
  border: 2px solid #F4672A;
  background: #fff3e0;
}

.detail-panel {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  height: fit-content;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.detail-empty {
  text-align: center;
  padding: 40px 0;
  color: #999;
}

.detail-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-record {
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
}

.record-info {
  display: flex;
  gap: 16px;
  margin-top: 8px;
  font-size: 13px;
  color: #666;
}
</style>
