package ru.enzhine.rtcms4j.notify.service.internal

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.mapper.toService
import ru.enzhine.rtcms4j.notify.repository.ConfigurationFeedbackEntityRepository
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationFeedbackEntity
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationKey
import ru.enzhine.rtcms4j.notify.service.internal.dto.ConfigurationFeedback
import java.time.LocalDateTime

@Service
class ConfigurationFeedbackServiceImpl(
    private val configurationFeedbackEntityRepository: ConfigurationFeedbackEntityRepository,
) : ConfigurationFeedbackService {
    override fun addConfigurationFeedback(
        namespaceId: Long,
        applicationId: Long,
        configurationId: Long,
        clientName: String,
        version: String,
    ): Mono<Boolean> {
        val configurationKey =
            ConfigurationKey(
                namespaceId = namespaceId,
                applicationId = applicationId,
                configurationId = configurationId,
            )

        val configurationFeedbackEntity =
            ConfigurationFeedbackEntity(
                timestamp = LocalDateTime.now(),
                clientName = clientName,
                version = version,
            )

        return configurationFeedbackEntityRepository
            .addConfigurationFeedbackAndDropOld(configurationKey, configurationFeedbackEntity)
    }

    override fun getConfigurationFeedback(
        namespaceId: Long,
        applicationId: Long,
        configurationId: Long,
    ): Flux<ConfigurationFeedback> {
        val configurationKey =
            ConfigurationKey(
                namespaceId = namespaceId,
                applicationId = applicationId,
                configurationId = configurationId,
            )

        return configurationFeedbackEntityRepository
            .getAllConfigurationFeedback(configurationKey)
            .map { it.toService() }
    }
}
