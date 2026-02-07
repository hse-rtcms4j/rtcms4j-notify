package ru.enzhine.rtcms4j.notify.repository.dto

import java.time.LocalDateTime

data class ApplicationFeedback(
    val timestamp: LocalDateTime,
    val clientName: String,
    val secretRotated: Boolean,
)
