package ru.enzhine.rtcms4j.notify.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import ru.enzhine.rtcms4j.core.api.event.NotificationEventDto
import ru.enzhine.rtcms4j.notify.config.props.KeyValRepositoryProperties
import ru.enzhine.rtcms4j.notify.listener.dto.NotificationEvent
import ru.enzhine.rtcms4j.notify.listener.dto.SubscribeKey
import ru.enzhine.rtcms4j.notify.mapper.toService

@Service
class NotifyEventListenerImpl(
    val listenerContainer: ReactiveRedisMessageListenerContainer,
    val objectMapper: ObjectMapper,
    keyValRepositoryProperties: KeyValRepositoryProperties,
) : NotifyEventListener {
    private val channelName = buildTopicKey(keyValRepositoryProperties)

    override fun subscribe(subscribeKey: SubscribeKey): Flux<NotificationEvent> =
        listenerContainer
            .receive(ChannelTopic.of(channelName))
            .map { parseNotificationEvent(it.message).toService() }
            .filter { filterNotificationEvent(subscribeKey, it) }

    private fun parseNotificationEvent(message: String): NotificationEventDto =
        objectMapper.readValue(message, NotificationEventDto::class.java)

    private fun filterNotificationEvent(
        subscribeKey: SubscribeKey,
        event: NotificationEvent,
    ): Boolean = event.namespaceId == subscribeKey.namespaceId && event.applicationId == subscribeKey.applicationId

    private fun buildTopicKey(properties: KeyValRepositoryProperties) = properties.globalPrefix + properties.topic
}
