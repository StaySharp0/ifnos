package com.ifnos.frame.auth.client

import feign.Logger
import feign.form.spring.SpringFormEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "keycloak-client",
    url = "\${keycloak.url}",
    configuration = [KeycloakClientConfig::class]
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

@Configuration
class KeycloakClientConfig {
    @Bean
    fun encoder(converters: ObjectFactory<HttpMessageConverters>) = SpringFormEncoder(SpringEncoder(converters))

    @Bean
    fun feignLoggerLevel() = Logger.Level.FULL
}