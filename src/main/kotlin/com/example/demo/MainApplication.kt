package com.example.demo

import com.example.demo.filter.JwtValidationFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
class MainApplication {

    @Bean
    fun filterRegistrationBean(filter: JwtValidationFilter): FilterRegistrationBean<JwtValidationFilter> {
        val registration = FilterRegistrationBean<JwtValidationFilter>()
        registration.filter = filter
        registration.addUrlPatterns("/users/*")
        registration.order = 1
        return registration
    }
}

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}