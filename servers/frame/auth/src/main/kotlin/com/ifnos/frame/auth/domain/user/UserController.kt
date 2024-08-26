package com.ifnos.frame.auth.domain.user

import com.ifnos.frame.auth.domain.keycloak.KeycloakUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: KeycloakUserService,
) {

    @PostMapping
    fun createUser(@RequestBody req: CreateUserRequest): ResponseEntity<Nothing> = req.run {
        userService.createUser(
            realmName = realm,
            email, name, password
        )

        ResponseEntity.ok(null)
    }
}

data class CreateUserRequest(
    val realm: String,
    val email: String,
    val name: String,
    val password: String,
)