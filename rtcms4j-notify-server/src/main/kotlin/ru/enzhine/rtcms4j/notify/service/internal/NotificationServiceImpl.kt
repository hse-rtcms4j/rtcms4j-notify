package ru.enzhine.rtcms4j.notify.service.internal

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.enzhine.rtcms4j.notify.listener.NotifyEventListener
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent
import ru.enzhine.rtcms4j.notify.listener.dto.SubscribeKey

@Service
class NotificationServiceImpl(
    private val notifyEventListener: NotifyEventListener,
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

        return notifyEventListener.subscribe(subscribeKey)
    }
}
