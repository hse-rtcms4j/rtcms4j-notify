package ru.enzhine.rtcms4j.notify.mapper

import ru.enzhine.rtcms4j.core.api.event.NotificationEventDto
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationFeedbackEntity
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationFeedbackEntity
import ru.enzhine.rtcms4j.notify.service.internal.dto.ApplicationFeedback
import ru.enzhine.rtcms4j.notify.service.internal.dto.ConfigurationFeedback

fun NotificationEventDto.toService() =
    NotificationEvent(
        namespaceId = namespaceId,
        applicationId = applicationId,
        secretRotatedEvent = secretRotatedEvent?.toService(),
        configUpdatedEvent = configurationUpdatedEvent?.toService(),
        isHeartbeat = false,
    )

fun NotificationEventDto.ConfigurationUpdatedEventDto.toService() =
    NotificationEvent.ConfigUpdatedEvent(
        configurationId = configurationId,
        payload = payload,
    )

fun NotificationEventDto.SecretRotatedEventDto.toService() =
    NotificationEvent.SecretRotatedEvent(
        newSecret = newSecret,
    )

fun ApplicationFeedbackEntity.toService() =
    ApplicationFeedback(
        timestamp = timestamp,
        clientName = clientName,
        secretRotated = secretRotated,
    )

fun ConfigurationFeedbackEntity.toService() =
    ConfigurationFeedback(
        timestamp = timestamp,
        clientName = clientName,
        version = version,
    )
