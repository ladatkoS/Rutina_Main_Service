package com.example.demo.config

import com.example.demo.filter.JwtValidationFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun jwtValidationFilterRegistration(filter: JwtValidationFilter): FilterRegistrationBean<JwtValidationFilter> {
        val registrationBean = FilterRegistrationBean<JwtValidationFilter>()
        registrationBean.filter = filter
        registrationBean.addUrlPatterns("/users/*") // фильтровать только запросы к users
        registrationBean.order = 1
        return registrationBean
    }
}