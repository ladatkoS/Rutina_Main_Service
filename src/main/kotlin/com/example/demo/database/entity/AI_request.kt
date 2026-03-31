package com.example.demo.database.entity

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Repository
/*
@Entity
@Table(name = "AI_request")
data class AI_request(
    @Column(name="name_of_habits", nullable = false)
    var name_of_habits: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "type", nullable = false)
    var type: String,

    @Column(name = "formation_period", nullable = false)
    var formationPeriod: String,
): AbstractEntity(){
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: Users
}
*/