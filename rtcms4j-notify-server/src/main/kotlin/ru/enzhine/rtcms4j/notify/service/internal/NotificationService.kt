package ru.enzhine.rtcms4j.notify.service.internal

import reactor.core.publisher.Flux
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent

interface NotificationService {
    fun subscribeOnNotificationEvents(
        namespaceId: Long,
        applicationId: Long,
    ): Flux<NotificationEvent>
}
