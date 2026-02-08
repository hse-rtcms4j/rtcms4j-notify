package ru.enzhine.rtcms4j.notify.repository.dto

import java.time.LocalDateTime

data class ConfigurationFeedbackEntity(
    val timestamp: LocalDateTime,
    val clientName: String,
    val version: String,
)
