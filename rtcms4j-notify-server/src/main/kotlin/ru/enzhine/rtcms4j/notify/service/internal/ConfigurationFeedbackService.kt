package ru.enzhine.rtcms4j.notify.service.internal

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.service.internal.dto.ConfigurationFeedback

interface ConfigurationFeedbackService {
    fun addConfigurationFeedback(
        namespaceId: Long,
        applicationId: Long,
        configurationId: Long,
        clientName: String,
        version: String,
    ): Mono<Boolean>

    fun getConfigurationFeedback(
        namespaceId: Long,
        applicationId: Long,
        configurationId: Long,
    ): Flux<ConfigurationFeedback>
}
