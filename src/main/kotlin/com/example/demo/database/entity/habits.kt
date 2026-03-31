package com.example.demo.database.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "habits")
data class habits(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nameOfHabit", nullable = false)
    var name: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "type", nullable = false)
    var type: String,

    @Column(name = "formationPeriod", nullable = false)
    var formationPeriod: Int,

    @Column(name = "endedAt")
    var endedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "createdAt")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    // Вместо связи ManyToOne - просто поле userId
    @Column(name = "user_id", nullable = false)
    var userId: Long  // только ID пользователя
)