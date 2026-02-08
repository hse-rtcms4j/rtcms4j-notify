package ru.enzhine.rtcms4j.notify.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "key-val-repository")
data class KeyValRepositoryProperties(
    val globalPrefix: String,
    val topic: String,
    val expireDuration: Duration,
)
