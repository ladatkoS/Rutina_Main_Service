package com.example.demo.model.dto.rq

import java.time.LocalDateTime

data class CreateHabitsRq(
    val name: String,
    val description: String,
    val type: String,
    val formationPeriod: Int,
)
