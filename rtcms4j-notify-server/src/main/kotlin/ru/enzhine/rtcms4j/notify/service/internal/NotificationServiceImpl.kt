package ru.enzhine.rtcms4j.notify.service.internal

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.enzhine.rtcms4j.notify.config.props.SseProperties
import ru.enzhine.rtcms4j.notify.listener.NotifyEventListener
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent
import ru.enzhine.rtcms4j.notify.listener.dto.SubscribeKey

@Service
class NotificationServiceImpl(
    private val notifyEventListener: NotifyEventListener,
    private val sseProperties: SseProperties,
) : NotificationService {
    override fun subscribeOnNotificationEvents(
        namespaceId: Long,
        applicationId: Long,
    ): Flux<NotificationEvent> {
        val subscribeKey =
            SubscribeKey(
                namespaceId = namespaceId,
                applicationId = applicationId,
            )

        val notificationsFlux =
            notifyEventListener.subscribe(subscribeKey)
        val heartbeatFlux =
            Flux.interval(sseProperties.heartbeatPeriod).map { heartbeatNotificationEvent(namespaceId, applicationId) }

        return Flux
            .merge(notificationsFlux, heartbeatFlux)
            .takeUntil { it.secretRotatedEvent != null }
    }

    private fun heartbeatNotificationEvent(
        namespaceId: Long,
        applicationId: Long,
    ) = NotificationEvent(
        namespaceId = namespaceId,
        applicationId = applicationId,
        secretRotatedEvent = null,
        configUpdatedEvent = null,
        isHeartbeat = true,
    )
}
