package ru.enzhine.rtcms4j.notify.service.internal.dto

import java.time.LocalDateTime

data class ConfigurationFeedback(
    val timestamp: LocalDateTime,
    val clientName: String,
    val version: String,
)
