package ru.enzhine.rtcms4j.notify.listener

import reactor.core.publisher.Flux
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent
import ru.enzhine.rtcms4j.notify.listener.dto.SubscribeKey

interface NotifyEventListener {
    fun subscribe(subscribeKey: SubscribeKey): Flux<NotificationEvent>
}
