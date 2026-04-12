package ru.enzhine.rtcms4j.notify.mapper

import ru.enzhine.rtcms4j.notify.api.dto.ApplicationFeedbackDto
import ru.enzhine.rtcms4j.notify.api.dto.ConfigurationFeedbackDto
import ru.enzhine.rtcms4j.notify.api.dto.ConfigurationUpdateEventDto
import ru.enzhine.rtcms4j.notify.api.dto.NotificationEventDto
import ru.enzhine.rtcms4j.notify.api.dto.PasswordRotationEventDto
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent
import ru.enzhine.rtcms4j.notify.service.internal.dto.ApplicationFeedback
import ru.enzhine.rtcms4j.notify.service.internal.dto.ConfigurationFeedback

fun ApplicationFeedback.toApi(): ApplicationFeedbackDto =
    ApplicationFeedbackDto(
        timestamp.toString(),
        clientName,
        secretRotated,
    )

fun ConfigurationFeedback.toApi(): ConfigurationFeedbackDto =
    ConfigurationFeedbackDto(
        timestamp.toString(),
        clientName,
        version,
    )

fun NotificationEvent.toApi(): NotificationEventDto =
    NotificationEventDto(
        configUpdatedEvent?.toApi(),
        secretRotatedEvent?.toApi(),
        isHeartbeat,
    )

fun NotificationEvent.ConfigUpdatedEvent.toApi(): ConfigurationUpdateEventDto =
    ConfigurationUpdateEventDto(
        configurationId,
        payload,
    )

fun NotificationEvent.SecretRotatedEvent.toApi(): PasswordRotationEventDto =
    PasswordRotationEventDto()
        .newPassword(newSecret)
