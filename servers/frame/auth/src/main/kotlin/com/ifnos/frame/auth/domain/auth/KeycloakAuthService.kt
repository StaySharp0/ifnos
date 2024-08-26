package com.ifnos.frame.auth.domain.auth

import com.ifnos.frame.auth.client.KeycloakClient
import org.springframework.stereotype.Service

@Service
class KeycloakAuthService(
    private val keycloakClient: KeycloakClient,
) {
    fun login(realm: String, email: String, password: String, client: String): Map<String, String> {

        // keycloak.realm(realm).clients().findByClientId(client).firstOrNull()
        //     ?: throw RuntimeException("Client with client_id $client not found")

        return keycloakClient.getToken(
            realm = realm,
            mapOf(
                "grant_type" to "password",
                "client_id" to client,
                "username" to email,
                "password" to password,
            ),
        )
    }

    fun refresh(realm: String, clientId: String, refreshToken: String): Map<String, String> {
        return keycloakClient.getToken(
            realm = realm,
            mapOf(
                "grant_type" to "refresh_token",
                "client_id" to clientId,
                "refresh_token" to refreshToken,
            ),
        )
    }
}
