package ru.enzhine.rtcms4j.notify.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "core-api")
data class SseProperties(
    val heartbeatPeriod: Duration = Duration.ofSeconds(30),
    val heartbeatDelay: Duration = Duration.ofSeconds(3),
)
