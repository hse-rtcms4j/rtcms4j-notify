package ru.enzhine.rtcms4j.notify.repository

import org.springframework.data.domain.Range
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.config.props.KeyValRepositoryProperties
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationFeedback
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationKey
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationFeedback
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationKey

@Repository
class KeyValueRepositoryImpl(
    private val applicationFeedbackTemplate: ReactiveRedisTemplate<String, ApplicationFeedback>,
    private val configurationFeedbackTemplate: ReactiveRedisTemplate<String, ConfigurationFeedback>,
    private val keyValRepositoryProperties: KeyValRepositoryProperties,
) : KeyValueRepository {
    override fun addApplicationFeedback(
        applicationKey: ApplicationKey,
        applicationFeedback: ApplicationFeedback,
    ): Mono<Boolean> {
        val key = buildApplicationFeedbackKey(keyValRepositoryProperties, applicationKey)

        val timestamp = System.currentTimeMillis().toDouble()
        val expireBound =
            System
                .currentTimeMillis()
                .minus(keyValRepositoryProperties.expireDuration.toMillis())
                .toDouble()

        return applicationFeedbackTemplate
            .opsForZSet()
            .add(key, applicationFeedback, timestamp)
            .flatMap { added ->
                if (added) {
                    applicationFeedbackTemplate
                        .opsForZSet()
                        .removeRangeByScore(key, Range.closed(0.0, expireBound))
                        .map { removed -> added }
                } else {
                    Mono.just(added)
                }
            }
    }

    override fun getLastApplicationFeedback(applicationKey: ApplicationKey): Flux<ApplicationFeedback> {
        val key = buildApplicationFeedbackKey(keyValRepositoryProperties, applicationKey)

        val expireBound =
            System
                .currentTimeMillis()
                .minus(keyValRepositoryProperties.expireDuration.toMillis())
                .toDouble()

        return applicationFeedbackTemplate
            .opsForZSet()
            .rangeByScore(key, Range.closed(expireBound, Double.MAX_VALUE))
    }

    override fun addConfigurationFeedback(
        configurationKey: ConfigurationKey,
        configurationFeedback: ConfigurationFeedback,
    ): Mono<Boolean> {
        val key = buildConfigurationFeedbackKey(keyValRepositoryProperties, configurationKey)

        val timestamp = System.currentTimeMillis().toDouble()
        val expireBound =
            System
                .currentTimeMillis()
                .minus(keyValRepositoryProperties.expireDuration.toMillis())
                .toDouble()

        return configurationFeedbackTemplate
            .opsForZSet()
            .add(key, configurationFeedback, timestamp)
            .flatMap { added ->
                if (added) {
                    configurationFeedbackTemplate
                        .opsForZSet()
                        .removeRangeByScore(key, Range.closed(0.0, expireBound))
                        .map { removed -> added }
                } else {
                    Mono.just(added)
                }
            }
    }

    override fun getLastConfigurationFeedback(configurationKey: ConfigurationKey): Flux<ConfigurationFeedback> {
        val key = buildConfigurationFeedbackKey(keyValRepositoryProperties, configurationKey)

        val expireBound =
            System
                .currentTimeMillis()
                .minus(keyValRepositoryProperties.expireDuration.toMillis())
                .toDouble()

        return configurationFeedbackTemplate
            .opsForZSet()
            .rangeByScore(key, Range.closed(expireBound, Double.MAX_VALUE))
    }

    private fun buildApplicationFeedbackKey(
        properties: KeyValRepositoryProperties,
        applicationKey: ApplicationKey,
    ) = "${properties.globalPrefix}${applicationKey.namespaceId}_${applicationKey.applicationId}" + "_APP_FEEDBACK"

    private fun buildConfigurationFeedbackKey(
        properties: KeyValRepositoryProperties,
        configurationKey: ConfigurationKey,
    ) = "${properties.globalPrefix}${configurationKey.namespaceId}_${configurationKey.applicationId}_${configurationKey.configurationId}" +
        "_CFG_FEEDBACK"
}
