import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useExerciseStore = defineStore('exercise', () => {
  const userId = ref(1)

  return {
    userId
  }
})
