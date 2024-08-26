package com.ifnos.frame.auth.domain.app

import com.ifnos.frame.auth.domain.app.model.App
import com.ifnos.frame.auth.domain.app.model.AppRepository
import com.ifnos.frame.auth.domain.keycloak.KeycloakAdminService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AppService(
    private val keycloakAdminService: KeycloakAdminService,
    private val appRepository: AppRepository,
) {
    @Transactional
    fun create(req: CreateAppRequest) = req.run {

        // keycloakAdminService.createClient()


        appRepository.save(App(name = name, description = description))
    }
}