package com.ifnos.frame.auth.domain.app.model

import org.springframework.data.jpa.repository.JpaRepository

interface AppRepository : JpaRepository<App, Long>