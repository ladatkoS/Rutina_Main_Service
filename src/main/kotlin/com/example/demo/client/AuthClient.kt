package com.example.demo.client

import com.example.demo.model.dto.rs.UserDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "auth-service", url = "\${auth.service.url}")
interface AuthClient {

    @GetMapping("/auth/validate")
    fun validateToken(@RequestHeader("Authorization") token: String): UserDto

    @PostMapping("/auth/user/{id}/increment-habits")
    fun incrementHabitsCount(@PathVariable("id") id: Long)

    @PostMapping("/auth/user/{id}/decrement-habits")
    fun decrementHabitsCount(@PathVariable("id") id: Long)

    @PostMapping("/auth/user/{id}/add-score")
    fun addScore(@PathVariable("id") id: Long, @RequestParam points: Int)
}