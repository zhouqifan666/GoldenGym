import request from './request'

export function addRecord(data) {
  return request.post('/exercise/record', data)
}

export function getRecords(params) {
  return request.get('/exercise/records', { params })
}

export function deleteRecord(id) {
  return request.delete(`/exercise/record/${id}`)
}

export function updateRecord(id, data) {
  return request.put(`/exercise/record/${id}`, data)
}

export function getWeekStats(userId) {
  return request.get('/exercise/stats/week', { params: { userId } })
}

export function getMonthStats(userId) {
  return request.get('/exercise/stats/month', { params: { userId } })
}

export function getTotalStats(userId) {
  return request.get('/exercise/stats/total', { params: { userId } })
}

export function getCalendarDates(year, month, userId) {
  return request.get(`/exercise/calendar/${year}/${month}`, { params: { userId } })
}

export function getRecordsByDate(date, userId) {
  return request.get(`/exercise/records/${date}`, { params: { userId } })
}
