package com.example.demo.model.mapper

import com.example.demo.database.entity.habits
import com.example.demo.model.dto.rq.CreateHabitsRq
import com.example.demo.model.dto.rs.HabitsDto

object HabitsMapper {

    fun CreateHabitsRq.toEntity(userId: Long): habits {
        return habits(
            name = this.name,
            description = this.description,
            type = this.type,
            formationPeriod = this.formationPeriod,
            userId = userId
        )
    }

    fun habits.toDto(): HabitsDto {
        return HabitsDto(
            id = this.id!!,
            name = this.name,
            description = this.description,
            type = this.type,
            formationPeriod = this.formationPeriod,
            createdAt = this.createdAt,
            endedAt = this.endedAt,
            userId = this.userId
        )
    }
}