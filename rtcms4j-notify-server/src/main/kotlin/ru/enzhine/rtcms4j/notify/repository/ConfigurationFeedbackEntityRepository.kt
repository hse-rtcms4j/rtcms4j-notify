package ru.enzhine.rtcms4j.notify.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationFeedbackEntity
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationKey

interface ConfigurationFeedbackEntityRepository {
    fun addConfigurationFeedbackAndDropOld(
        configurationKey: ConfigurationKey,
        configurationFeedbackEntity: ConfigurationFeedbackEntity,
    ): Mono<Boolean>

    fun getAllConfigurationFeedback(configurationKey: ConfigurationKey): Flux<ConfigurationFeedbackEntity>
}
