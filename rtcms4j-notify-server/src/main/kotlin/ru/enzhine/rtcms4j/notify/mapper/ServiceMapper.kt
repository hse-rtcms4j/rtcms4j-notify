package ru.enzhine.rtcms4j.notify.mapper

import ru.enzhine.rtcms4j.core.api.event.NotificationEventDto
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent

fun NotificationEventDto.toService() =
    NotificationEvent(
        namespaceId = namespaceId,
        applicationId = applicationId,
        secretRotatedEvent = secretRotatedEvent?.toService(),
        configUpdatedEvent = configurationUpdatedEvent?.toService(),
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
