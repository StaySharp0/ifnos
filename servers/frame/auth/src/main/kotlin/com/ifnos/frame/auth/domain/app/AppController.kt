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
    private val appService: AppService,
) {

    @PostMapping
    fun createStore(@RequestBody req: CreateAppRequest): ResponseEntity<App> = appService.create(req).run {
        ResponseEntity.ok(this)
    }
}

data class CreateAppRequest(val name: String, val description: String)