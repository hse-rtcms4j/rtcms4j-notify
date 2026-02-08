package ru.enzhine.rtcms4j.notify.service.internal

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.service.internal.dto.ApplicationFeedback

interface ApplicationFeedbackService {
    fun addApplicationFeedback(
        namespaceId: Long,
        applicationId: Long,
        clientName: String,
        isSecretRotated: Boolean,
    ): Mono<Boolean>

    fun getApplicationFeedback(
        namespaceId: Long,
        applicationId: Long,
    ): Flux<ApplicationFeedback>
}
