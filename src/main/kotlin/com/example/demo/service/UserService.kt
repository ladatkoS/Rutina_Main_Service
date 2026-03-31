package com.example.demo.service

import com.example.demo.model.dto.rs.HabitsDto

interface UserService {
    fun createHabits(userId: Long, name: String, description: String, type: String, formationPeriod: Int): HabitsDto
    fun getAllHabits(userId: Long): List<HabitsDto>
    fun getHabitById(userId: Long, habitId: Long): HabitsDto
    fun deleteHabit(userId: Long, habitId: Long)
}