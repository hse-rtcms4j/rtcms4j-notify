package ru.enzhine.rtcms4j.notify.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationFeedback
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationKey
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationFeedback
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationKey

interface KeyValueRepository {
    fun addApplicationFeedback(
        applicationKey: ApplicationKey,
        applicationFeedback: ApplicationFeedback,
    ): Mono<Boolean>

    fun getLastApplicationFeedback(applicationKey: ApplicationKey): Flux<ApplicationFeedback>

    fun addConfigurationFeedback(
        configurationKey: ConfigurationKey,
        configurationFeedback: ConfigurationFeedback,
    ): Mono<Boolean>

    fun getLastConfigurationFeedback(configurationKey: ConfigurationKey): Flux<ConfigurationFeedback>
}
