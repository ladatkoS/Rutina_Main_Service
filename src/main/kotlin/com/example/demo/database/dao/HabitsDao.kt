package com.example.demo.database.dao

import com.example.demo.database.entity.habits
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface HabitsDao: CrudRepository<habits, Long> {

    fun getByUserId(userId: Long): List<habits>

    fun getByUserIdAndId(userId: Long, id: Long): habits?

    @Modifying
    @Query("DELETE FROM habits h WHERE h.id = :habitId AND h.userId = :userId")
    fun deleteByIdAndUserId(@Param("habitId") habitId: Long, @Param("userId") userId: Long): Int

    fun findAllByEndedAtBefore(date: LocalDateTime): List<habits>
}