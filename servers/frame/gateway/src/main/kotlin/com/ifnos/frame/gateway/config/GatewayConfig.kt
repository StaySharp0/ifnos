package com.ifnos.frame.gateway.config

import com.ifnos.frame.gateway.filter.TokenFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfig(
    @Value("\${server-url.auth}")
    private val authServiceUrl: String,
    private val tokenFilter: TokenFilter,
) {
    
    @Bean
    fun authRoute(builder: RouteLocatorBuilder): RouteLocator = builder.routes {
        route(
            "auth-public-route",
        ) {
            path("/api/auth")
            filters {
                filter(tokenFilter.apply(TokenFilter.Config()))
            }
            uri(authServiceUrl)
        }
    }
}