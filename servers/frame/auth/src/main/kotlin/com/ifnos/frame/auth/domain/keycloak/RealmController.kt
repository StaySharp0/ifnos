package com.ifnos.frame.auth.domain.keycloak

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/internal/keycloak/realm")
class RealmController(private val keycloakAdminService: KeycloakAdminService) {

    @PostMapping
    fun createRealm(@RequestBody req: CreateRealmRequest): ResponseEntity<Nothing> = req.run {
        keycloakAdminService.createRealm(realmName)
        ResponseEntity.ok(null)
    }
}

data class CreateRealmRequest(val realmName: String)