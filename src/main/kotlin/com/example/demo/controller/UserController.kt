package com.example.demo.controller

import com.example.demo.model.dto.rq.CreateHabitsRq
import com.example.demo.model.dto.rs.HabitsDto
import com.example.demo.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/createHabits")
    fun createHabits(
        @RequestBody request: CreateHabitsRq,
        @RequestAttribute("userId") userId: Long
    ): HabitsDto {
        println("🚀 Контроллер: запрос на создание привычки")
        println("userId из атрибута: $userId")
        println("request: $request")

        val result = userService.createHabits(
            userId = userId,
            name = request.name,
            description = request.description,
            type = request.type,
            formationPeriod = request.formationPeriod
        )

        println("✅ Контроллер: привычка создана, id: ${result.id}")
        return result
    }

    @GetMapping("/getHabits")
    fun getAllHabits(
        @RequestAttribute("userId") userId: Long  // получаем из фильтра
    ): List<HabitsDto> {
        return userService.getAllHabits(userId)
    }

    @GetMapping("/getHabits/{habitId}")
    fun getHabitById(
        @PathVariable habitId: Long,
        @RequestAttribute("userId") userId: Long  // получаем из фильтра
    ): HabitsDto {
        return userService.getHabitById(userId, habitId)
    }

    @DeleteMapping("/deleteHabits/{habitId}")
    fun deleteHabits(
        @PathVariable habitId: Long,
        @RequestAttribute("userId") userId: Long  // получаем из фильтра
    ) {
        userService.deleteHabit(userId, habitId)
    }
}