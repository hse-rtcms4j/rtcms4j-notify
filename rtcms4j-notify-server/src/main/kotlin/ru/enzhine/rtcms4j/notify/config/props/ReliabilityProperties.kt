package ru.enzhine.rtcms4j.notify.config.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "reliability")
data class ReliabilityProperties(
    val strict: Boolean = true,
)
