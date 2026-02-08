package ru.enzhine.rtcms4j.notify.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationFeedbackEntity
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationKey

interface ApplicationFeedbackEntityRepository {
    fun addApplicationFeedbackAndDropOld(
        applicationKey: ApplicationKey,
        applicationFeedbackEntity: ApplicationFeedbackEntity,
    ): Mono<Boolean>

    fun getAllApplicationFeedback(applicationKey: ApplicationKey): Flux<ApplicationFeedbackEntity>
}
