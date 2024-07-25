package com.ifnos.frame.domain

import org.apache.commons.validator.routines.UrlValidator

sealed class AppId(open val value: String) {
    data class UserApp(override val value: String) : AppId(value) {
        init {
            require(value != FRAME_APP_ID) { "Invalid AppId" }
        }
    }

    data object FrameApp : AppId(FRAME_APP_ID)

    companion object {
        const val FRAME_APP_ID = "__frame__"
    }
}

@JvmInline
value class URL(val address: String?) {
    init {
        require(UrlValidator(arrayOf("http", "https")).isValid(address)) {
            "Invalid URL format"
        }
    }
}
