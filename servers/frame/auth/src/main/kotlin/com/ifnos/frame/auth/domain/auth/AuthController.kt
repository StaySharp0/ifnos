package com.ifnos.frame.auth.domain.auth

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


    // @PostMapping("/refresh")
    // fun refreshToken(
    //     @RequestParam @NotBlank refreshToken: String,
    // ): ResponseEntity<Map<String, String>> {
    //     return try {
    //         val accessToken: AccessToken = keycloakAuthService.refreshToken(refreshToken)
    //         ResponseEntity.ok(mapOf("access_token" to accessToken.token))
    //     } catch (e: Exception) {
    //         ResponseEntity.badRequest().body(mapOf("error" to e.message))
    //     }
    // }
}

data class LoginRequest(
    @Email @NotBlank val email: String,
    @NotBlank val password: String,
    val client: String = "portal-cli",
)