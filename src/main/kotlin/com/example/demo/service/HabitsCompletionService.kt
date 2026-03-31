package com.example.demo.service

import com.example.demo.database.entity.habits

interface HabitsCompletionService {
    fun checkAndCompleteHabits()


    fun completeHabit(habit: habits)
}