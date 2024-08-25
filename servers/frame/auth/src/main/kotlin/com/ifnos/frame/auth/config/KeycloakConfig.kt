package com.ifnos.frame.auth.config

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfig(
    @Value("\${keycloak.url}") private val serverUrl: String,
    @Value("\${keycloak.realm}") private val realm: String,
    @Value("\${keycloak.client-id}") private val clientId: String,
    @Value("\${keycloak.username}") private val username: String,
    @Value("\${keycloak.password}") private val password: String,
) {

    @Bean
    fun keycloak(): Keycloak = KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .clientId(clientId)
        .username(username)
        .password(password)
        .build()
}