package com.ifnos.frame.auth.domain.app

import com.ifnos.frame.auth.domain.app.model.App
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/app")
class AppController(
    private val appSvc: AppService,
) {

    @PostMapping
    fun createApp(
        realm: String = "test-realm", /* TODO: 헤더에서 추출 */
        @RequestBody req: CreateAppRequest,
    ): ResponseEntity<App> = req.run {
        ResponseEntity.ok(appSvc.create(realm, name, description))
    }
}

data class CreateAppRequest(val name: String, val description: String)