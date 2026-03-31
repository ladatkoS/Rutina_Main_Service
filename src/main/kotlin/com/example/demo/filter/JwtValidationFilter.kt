package com.example.demo.filter

import com.example.demo.client.AuthClient
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtValidationFilter(
    private val authClient: AuthClient
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("🔍 Фильтр работает!")
        val token = request.getHeader("Authorization")
        println("Токен: $token")

        if (token != null && token.startsWith("Bearer ")) {
            try {
                val userDto = authClient.validateToken(token)
                println("✅ Пользователь: ${userDto.email}")
                request.setAttribute("userId", userDto.id)
                filterChain.doFilter(request, response)
            } catch (e: Exception) {
                println("❌ Ошибка: ${e.message}")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
            }
        } else {
            println("❌ Нет токена")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token")
        }
    }
}