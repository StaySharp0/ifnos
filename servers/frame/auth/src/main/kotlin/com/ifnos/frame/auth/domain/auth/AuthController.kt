package com.ifnos.frame.auth.domain.auth

import com.ifnos.frame.auth.domain.keycloak.KeycloakConstant.Companion.PORTAL_CLI_NAME
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val keycloakAuthService: KeycloakAuthService) {

    @PostMapping("{realm}/login")
    fun login(
        @PathVariable("realm") realm: String,
        @RequestBody @Valid req: LoginRequest,
    ) = req.run {
        keycloakAuthService.login(realm, email, password, client)
    }

    @PostMapping("{realm}/refresh")
    fun refresh(
        @PathVariable("realm") realm: String,
        @RequestBody req: RefreshRequest,
    ) = req.run {
        keycloakAuthService.refresh(realm, clientId, refreshToken)
    }
}

data class LoginRequest(
    @Email @NotBlank val email: String,
    @NotBlank val password: String,
    val client: String = PORTAL_CLI_NAME,
)

data class RefreshRequest(
    val clientId: String,
    val refreshToken: String,
)