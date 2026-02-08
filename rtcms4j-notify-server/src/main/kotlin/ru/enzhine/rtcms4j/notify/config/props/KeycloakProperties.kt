package ru.enzhine.rtcms4j.notify.config.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "keycloak")
data class KeycloakProperties(
    val resourcePrefix: String,
)
