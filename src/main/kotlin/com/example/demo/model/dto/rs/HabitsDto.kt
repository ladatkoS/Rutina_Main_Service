package com.example.demo.model.dto.rs

import java.time.LocalDateTime

data class HabitsDto(
    val id: Long,
    val name: String,
    val description: String,
    val type: String,
    val formationPeriod: Int,
    val createdAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val userId: Long,
)
