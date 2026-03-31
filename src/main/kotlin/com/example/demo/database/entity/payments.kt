package com.example.demo.database.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
/*
@Entity
@Table(name = "payments")
data class payments(
    @Column(name = "status", nullable = false)
    var status: String,

    @Column(name = "cost")
    var cost: Int = 0,

    @Column(name = "payment_method")
    var paymentMethod: String = "",

    @Column(name = "created_at")
    @CreationTimestamp
    var createdAt: LocalDateTime = LocalDateTime.now()
): AbstractEntity(){
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: Users
}
*/