import request from './request'

export function createGoal(data) {
  return request.post('/goal/create', data)
}

export function listGoals(userId) {
  return request.get('/goal/list', { params: { userId } })
}

export function toggleComplete(id) {
  return request.put(`/goal/${id}/complete`)
}

export function deleteGoal(id) {
  return request.delete(`/goal/${id}`)
}
