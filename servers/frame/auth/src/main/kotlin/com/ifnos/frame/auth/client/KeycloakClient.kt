package com.ifnos.frame.auth.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "keycloak-client",
    url = "\${keycloak.url}"
)
interface KeycloakClient {
    @PostMapping(
        "/realms/{realm}/protocol/openid-connect/token",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    )
    fun getToken(
        @PathVariable realm: String,
        @RequestBody request: Map<String, *>,
    ): Map<String, String>
}