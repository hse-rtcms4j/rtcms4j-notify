package ru.enzhine.rtcms4j.notify.repository

import org.springframework.data.domain.Range
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.config.props.KeyValRepositoryProperties
import ru.enzhine.rtcms4j.notify.ext.toEpochMillis
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationFeedbackEntity
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationKey
import java.time.LocalDateTime

@Repository
class ApplicationFeedbackEntityRepositoryImpl(
    private val applicationFeedbackEntityTemplate: ReactiveRedisTemplate<String, ApplicationFeedbackEntity>,
    private val keyValRepositoryProperties: KeyValRepositoryProperties,
) : ApplicationFeedbackEntityRepository {
    override fun addApplicationFeedbackAndDropOld(
        applicationKey: ApplicationKey,
        applicationFeedbackEntity: ApplicationFeedbackEntity,
    ): Mono<Boolean> {
        val key = buildApplicationFeedbackKey(keyValRepositoryProperties, applicationKey)

        val timestamp =
            applicationFeedbackEntity.timestamp
                .toEpochMillis()
                .toDouble()
        val expireBound =
            applicationFeedbackEntity.timestamp
                .minus(keyValRepositoryProperties.expireDuration)
                .toEpochMillis()
                .toDouble()

        return applicationFeedbackEntityTemplate
            .opsForZSet()
            .add(key, applicationFeedbackEntity, timestamp)
            .flatMap { added ->
                if (added) {
                    applicationFeedbackEntityTemplate
                        .opsForZSet()
                        .removeRangeByScore(key, Range.closed(0.0, expireBound))
                        .map { removed -> added }
                } else {
                    Mono.just(added)
                }
            }
    }

    override fun getAllApplicationFeedback(applicationKey: ApplicationKey): Flux<ApplicationFeedbackEntity> {
        val key = buildApplicationFeedbackKey(keyValRepositoryProperties, applicationKey)

        val expireBound =
            LocalDateTime
                .now()
                .minus(keyValRepositoryProperties.expireDuration)
                .toEpochMillis()
                .toDouble()

        return applicationFeedbackEntityTemplate
            .opsForZSet()
            .rangeByScore(key, Range.closed(expireBound, Double.MAX_VALUE))
    }

    private fun buildApplicationFeedbackKey(
        properties: KeyValRepositoryProperties,
        applicationKey: ApplicationKey,
    ) = "${properties.globalPrefix}${applicationKey.namespaceId}_${applicationKey.applicationId}" + "_APP_FEEDBACK"
}
