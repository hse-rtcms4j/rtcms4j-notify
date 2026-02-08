package ru.enzhine.rtcms4j.notify.config.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "core-api")
data class CoreApiProperties(
    val baseUrl: String,
)
