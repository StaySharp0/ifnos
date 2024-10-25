package com.ifnos.frame.auth.domain.keycloak

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.system.measureTimeMillis

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LargeScaleConfigurationTest {

    private val realmName = "test-realm"
    private val threadCount = 10
    private val batchSize = 1000

    @Test
    @Order(1)
    fun testCreateRealm() = runBlocking {
        val response = client.post<HttpResponse>("http://localhost:20100/api/internal/keycloak/realm") {
            contentType(ContentType.Application.Json)
            body = """{"realmName": "$realmName"}"""
        }
        assertEquals(200, response.status.value, "Realm creation failed.")
        println("Created realm: $realmName")
    }

    @Test
    @Order(2)
    fun testCreateOneMillionApps() = runBlocking {
        val appCreationTime = measureTimeMillis {
            val jobs = List(threadCount) {
                launch(Dispatchers.IO) {
                    repeat(1_000_000 / threadCount) { appIndex ->
                        val globalAppIndex = it * (1_000_000 / threadCount) + appIndex + 1
                        val appName = "app $globalAppIndex"
                        val appDescription = "custom app$globalAppIndex"
                        val response = client.post<HttpResponse>("http://localhost:20100/api/app") {
                            contentType(ContentType.Application.Json)
                            body = """{
                                "name": "$appName",
                                "description": "$appDescription"
                            }"""
                        }
                        assertEquals(200, response.status.value, "App creation failed at index $globalAppIndex.")

                        if (globalAppIndex % batchSize == 0) {
                            println("Thread ${it + 1} created $batchSize apps")
                        }
                    }
                }
            }
            jobs.joinAll()
        }
        println("Time taken to create 1 million apps: $appCreationTime ms")
    }

    @Test
    @Order(3)
    fun testCreateTenMillionUsers() = runBlocking {
        val userCreationTime = measureTimeMillis {
            val jobs = List(threadCount) {
                launch(Dispatchers.IO) {
                    repeat(10_000_000 / threadCount) { userIndex ->
                        val globalUserIndex = it * (10_000_000 / threadCount) + userIndex + 1
                        val email = "test$globalUserIndex@test.com"
                        val name = "tester$globalUserIndex"
                        val password = "qwer1234!@"
                        val response = client.post<HttpResponse>("http://localhost:20100/api/user") {
                            contentType(ContentType.Application.Json)
                            body = """{
                                "realm": "$realmName",
                                "email": "$email",
                                "name": "$name",
                                "password": "$password"
                            }"""
                        }
                        assertEquals(200, response.status.value, "User creation failed at index $globalUserIndex.")

                        if (globalUserIndex % batchSize == 0) {
                            println("Thread ${it + 1} created $batchSize users")
                        }
                    }
                }
            }
            jobs.joinAll()
        }
        println("Time taken to create 10 million users: $userCreationTime ms")
    }

    companion object {
        @JvmStatic
        val client = HttpClient()

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit {
            client.close()
        }
    }
}
