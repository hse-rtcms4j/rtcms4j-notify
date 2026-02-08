package ru.enzhine.rtcms4j.notify.repository

import org.springframework.data.domain.Range
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.config.props.KeyValRepositoryProperties
import ru.enzhine.rtcms4j.notify.ext.toEpochMillis
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationFeedbackEntity
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationKey
import java.time.LocalDateTime

@Repository
class ConfigurationFeedbackEntityRepositoryImpl(
    private val configurationFeedbackEntityTemplate: ReactiveRedisTemplate<String, ConfigurationFeedbackEntity>,
    private val keyValRepositoryProperties: KeyValRepositoryProperties,
) : ConfigurationFeedbackEntityRepository {
    override fun addConfigurationFeedbackAndDropOld(
        configurationKey: ConfigurationKey,
        configurationFeedbackEntity: ConfigurationFeedbackEntity,
    ): Mono<Boolean> {
        val key = buildConfigurationFeedbackKey(keyValRepositoryProperties, configurationKey)

        val timestamp =
            configurationFeedbackEntity.timestamp
                .toEpochMillis()
                .toDouble()
        val expireBound =
            configurationFeedbackEntity.timestamp
                .minus(keyValRepositoryProperties.expireDuration)
                .toEpochMillis()
                .toDouble()

        return configurationFeedbackEntityTemplate
            .opsForZSet()
            .add(key, configurationFeedbackEntity, timestamp)
            .flatMap { added ->
                if (added) {
                    configurationFeedbackEntityTemplate
                        .opsForZSet()
                        .removeRangeByScore(key, Range.closed(0.0, expireBound))
                        .map { removed -> added }
                } else {
                    Mono.just(added)
                }
            }
    }

    override fun getAllConfigurationFeedback(configurationKey: ConfigurationKey): Flux<ConfigurationFeedbackEntity> {
        val key = buildConfigurationFeedbackKey(keyValRepositoryProperties, configurationKey)

        val expireBound =
            LocalDateTime
                .now()
                .minus(keyValRepositoryProperties.expireDuration)
                .toEpochMillis()
                .toDouble()

        return configurationFeedbackEntityTemplate
            .opsForZSet()
            .rangeByScore(key, Range.closed(expireBound, Double.MAX_VALUE))
    }

    private fun buildConfigurationFeedbackKey(
        properties: KeyValRepositoryProperties,
        configurationKey: ConfigurationKey,
    ) = "${properties.globalPrefix}${configurationKey.namespaceId}_${configurationKey.applicationId}_${configurationKey.configurationId}" +
        "_CFG_FEEDBACK"
}
