package com.ifnos.frame.domain

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

class URLTest {
    @ParameterizedTest(name = "Test#{index} - address: ''{0}''")
    @NullAndEmptySource
    @ValueSource(strings = [" ", "qwer"])
    fun `URL format 유효하지 않는 경우`(value: String?) {
        assertThrows<IllegalArgumentException> { URL(value) }
    }

    @ParameterizedTest(name = "Test#{index} - address: ''{0}''")
    @MethodSource("validProvider")
    fun `URL format 유효한 경우`(value: String) {
        assertDoesNotThrow { URL(value) }
    }

    companion object {
        @JvmStatic
        fun validProvider(): List<String> {
            val origins = listOf("http://ifnos.com", "https://ifnos.com")
            val variable = listOf("", "#hash", "?no-value-query", "?query=value", "/route")

            return variable.flatMap { origins.map { origin -> origin + it } }
        }
    }
}
