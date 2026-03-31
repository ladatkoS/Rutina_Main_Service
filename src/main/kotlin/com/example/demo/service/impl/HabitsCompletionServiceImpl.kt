package com.example.demo.service.impl

import com.example.demo.client.AuthClient
import com.example.demo.database.dao.HabitsDao
import com.example.demo.database.entity.habits
import com.example.demo.service.HabitsCompletionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class HabitsCompletionServiceImpl(
    private val habitsDao: HabitsDao,
    private val authClient: AuthClient
) : HabitsCompletionService {

    @Scheduled(fixedDelay = 60000)
    @Transactional
    override fun checkAndCompleteHabits() {
        val habitsToDelete = habitsDao.findAllByEndedAtBefore(LocalDateTime.now())

        if (habitsToDelete.isNotEmpty()) {
            habitsToDelete.forEach { completeHabit(it) }
        }
    }

    @Transactional
    override fun completeHabit(habit: habits) {
        authClient.addScore(habit.userId, 1)
        habitsDao.delete(habit)
        authClient.decrementHabitsCount(habit.userId)
    }
}