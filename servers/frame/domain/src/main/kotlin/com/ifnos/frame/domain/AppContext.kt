package com.ifnos.frame.domain

sealed class AppContext(open val id: AppId, open var lastVisitedURL: URL)

data class FrameAppContext(override var lastVisitedURL: URL, var theme: Theme) :
    AppContext(AppId.FrameApp, lastVisitedURL)

data class UserAppContext(override val id: AppId, override var lastVisitedURL: URL) :
    AppContext(id, lastVisitedURL)

sealed class Theme() {
    data object Light : Theme()
    data object Dark : Theme()
    data object Auto : Theme()

    /* TODO: Custom Theme */

    companion object {
        fun valueOf(value: String) = when (value) {
            "light" -> Light
            "dark" -> Dark
            "Auto" -> Auto
            else -> throw IllegalArgumentException("Invalid theme")
        }
    }
}
