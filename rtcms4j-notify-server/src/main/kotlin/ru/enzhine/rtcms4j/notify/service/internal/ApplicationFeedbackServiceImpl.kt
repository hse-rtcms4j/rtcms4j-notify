package ru.enzhine.rtcms4j.notify.service.internal

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.mapper.toService
import ru.enzhine.rtcms4j.notify.repository.ApplicationFeedbackEntityRepository
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationFeedbackEntity
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationKey
import ru.enzhine.rtcms4j.notify.service.internal.dto.ApplicationFeedback
import java.time.LocalDateTime

@Service
class ApplicationFeedbackServiceImpl(
    private val applicationFeedbackEntityRepository: ApplicationFeedbackEntityRepository,
) : ApplicationFeedbackService {
    override fun addApplicationFeedback(
        namespaceId: Long,
        applicationId: Long,
        clientName: String,
        isSecretRotated: Boolean,
    ): Mono<Boolean> {
        val applicationKey =
            ApplicationKey(
                namespaceId = namespaceId,
                applicationId = applicationId,
            )

        val applicationFeedbackEntity =
            ApplicationFeedbackEntity(
                timestamp = LocalDateTime.now(),
                clientName = clientName,
                secretRotated = isSecretRotated,
            )

        return applicationFeedbackEntityRepository
            .addApplicationFeedbackAndDropOld(applicationKey, applicationFeedbackEntity)
    }

    override fun getApplicationFeedback(
        namespaceId: Long,
        applicationId: Long,
    ): Flux<ApplicationFeedback> {
        val applicationKey =
            ApplicationKey(
                namespaceId = namespaceId,
                applicationId = applicationId,
            )

        return applicationFeedbackEntityRepository
            .getAllApplicationFeedback(applicationKey)
            .map { it.toService() }
    }
}
