package com.example.demo.service.impl

import com.example.demo.client.AuthClient
import com.example.demo.database.dao.HabitsDao
import com.example.demo.model.dto.rq.CreateHabitsRq
import com.example.demo.model.dto.rs.HabitsDto
import com.example.demo.model.mapper.HabitsMapper.toDto
import com.example.demo.model.mapper.HabitsMapper.toEntity
import com.example.demo.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val habitsDao: HabitsDao,
    private val authClient: AuthClient
) : UserService {

    @Transactional
    override fun createHabits(
        userId: Long,
        name: String,
        description: String,
        type: String,
        formationPeriod: Int
    ): HabitsDto {

        val request = CreateHabitsRq(
            name = name,
            description = description,
            type = type,
            formationPeriod = formationPeriod
        )

        val habits = request.toEntity(userId)
        habits.endedAt = LocalDateTime.now().plusMinutes(formationPeriod.toLong())

        val savedHabits = habitsDao.save(habits)

        authClient.incrementHabitsCount(userId)

        return savedHabits.toDto()
    }

    override fun getAllHabits(userId: Long): List<HabitsDto> {
        return habitsDao.getByUserId(userId).map { it.toDto() }
    }

    override fun getHabitById(userId: Long, habitId: Long): HabitsDto {
        val habit = habitsDao.getByUserIdAndId(userId, habitId)
            ?: throw RuntimeException("Привычка с id $habitId не найдена")
        return habit.toDto()
    }

    @Transactional
    override fun deleteHabit(userId: Long, habitId: Long) {
        val deletedCount = habitsDao.deleteByIdAndUserId(habitId, userId)

        if (deletedCount == 0) {
            throw RuntimeException("Привычка с id $habitId не найдена")
        }

        authClient.decrementHabitsCount(userId)
    }
}