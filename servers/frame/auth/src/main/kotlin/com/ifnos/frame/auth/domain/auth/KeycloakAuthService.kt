package com.ifnos.frame.auth.domain.auth

import com.ifnos.frame.auth.client.KeycloakClient
import org.keycloak.admin.client.Keycloak
import org.springframework.stereotype.Service

@Service
class KeycloakAuthService(
    private val keycloak: Keycloak,
    private val keycloakClient: KeycloakClient,
) {
    fun login(realm: String, email: String, password: String, client: String): Map<String, String> {

        keycloak.realm(realm).clients().findByClientId(client).firstOrNull()
            ?: throw RuntimeException("Client with client_id $client not found")

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
}
