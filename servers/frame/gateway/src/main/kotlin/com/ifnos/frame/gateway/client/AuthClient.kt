package com.ifnos.frame.gateway.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "auth-client",
    url = "\${service-url.auth}",
)
interface AuthClient {

    @GetMapping("/auth/jwks")
    fun getJwkSet(): Map<String, Any>

    @GetMapping("/auth/refresh-token")
    fun refreshToken(@RequestParam refreshToken: String): String
}