package ru.enzhine.rtcms4j.notify.mapper

import ru.enzhine.rtcms4j.notify.api.dto.ApplicationFeedbackDto
import ru.enzhine.rtcms4j.notify.api.dto.ConfigurationFeedbackDto
import ru.enzhine.rtcms4j.notify.api.dto.ConfigurationUpdateEventDto
import ru.enzhine.rtcms4j.notify.api.dto.NotificationEventDto
import ru.enzhine.rtcms4j.notify.api.dto.PasswordRotationEventDto
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent
import ru.enzhine.rtcms4j.notify.service.internal.dto.ApplicationFeedback
import ru.enzhine.rtcms4j.notify.service.internal.dto.ConfigurationFeedback

fun ApplicationFeedback.toApi() =
    ApplicationFeedbackDto(
        timestamp.toString(),
        clientName,
        secretRotated,
    )

fun ConfigurationFeedback.toApi() =
    ConfigurationFeedbackDto(
        timestamp.toString(),
        clientName,
        version,
    )

fun NotificationEvent.toApi() =
    NotificationEventDto(
        configUpdatedEvent?.toApi(),
        secretRotatedEvent?.toApi(),
    )

fun NotificationEvent.ConfigUpdatedEvent.toApi() =
    ConfigurationUpdateEventDto(
        configurationId,
        payload,
    )

fun NotificationEvent.SecretRotatedEvent.toApi() =
    PasswordRotationEventDto(
        newSecret,
    )
