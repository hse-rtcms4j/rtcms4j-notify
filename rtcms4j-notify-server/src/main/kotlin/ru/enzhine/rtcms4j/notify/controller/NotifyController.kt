package ru.enzhine.rtcms4j.notify.controller

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.api.NotifyApi
import ru.enzhine.rtcms4j.notify.api.dto.ApplicationFeedbackRequestDto
import ru.enzhine.rtcms4j.notify.api.dto.ApplicationFeedbackResponseDto
import ru.enzhine.rtcms4j.notify.api.dto.ConfigurationFeedbackRequestDto
import ru.enzhine.rtcms4j.notify.api.dto.ConfigurationFeedbackResponseDto
import ru.enzhine.rtcms4j.notify.api.dto.NotificationEventDto
import ru.enzhine.rtcms4j.notify.mapper.toApi
import ru.enzhine.rtcms4j.notify.security.dto.KeycloakPrincipal
import ru.enzhine.rtcms4j.notify.service.external.AccessControlService
import ru.enzhine.rtcms4j.notify.service.internal.ApplicationFeedbackService
import ru.enzhine.rtcms4j.notify.service.internal.ConfigurationFeedbackService
import ru.enzhine.rtcms4j.notify.service.internal.NotificationService

@RestController
@RequestMapping("/api/v1")
class NotifyController(
    private val applicationFeedbackService: ApplicationFeedbackService,
    private val configurationFeedbackService: ConfigurationFeedbackService,
    private val notificationService: NotificationService,
    private val accessControlService: AccessControlService,
) : NotifyApi {
    override fun getApplicationFeedback(
        nid: Long,
        aid: Long,
        exchange: ServerWebExchange?,
    ): Mono<ApplicationFeedbackResponseDto> =
        currentPrincipal()
            .flatMap { keycloakPrincipal ->
                accessControlService
                    .hasAccessToApplication(
                        keycloakPrincipal = keycloakPrincipal,
                        namespaceId = nid,
                        applicationId = aid,
                    ).flatMap { hasAccess ->
                        if (hasAccess) {
                            applicationFeedbackService
                                .getApplicationFeedback(
                                    namespaceId = nid,
                                    applicationId = aid,
                                ).map { it.toApi() }
                                .collectList()
                                .map { ApplicationFeedbackResponseDto(it) }
                        } else {
                            Mono.error(AccessDeniedException("At least application manager or program-client required."))
                        }
                    }
            }

    override fun postApplicationFeedback(
        nid: Long,
        aid: Long,
        applicationFeedbackRequestDto: Mono<ApplicationFeedbackRequestDto>,
        exchange: ServerWebExchange?,
    ): Mono<Void> =
        applicationFeedbackRequestDto.flatMap { request ->
            currentPrincipal()
                .flatMap { keycloakPrincipal ->
                    accessControlService
                        .hasAccessToApplication(
                            keycloakPrincipal = keycloakPrincipal,
                            namespaceId = nid,
                            applicationId = aid,
                        ).flatMap { hasAccess ->
                            if (hasAccess) {
                                applicationFeedbackService
                                    .addApplicationFeedback(
                                        namespaceId = nid,
                                        applicationId = aid,
                                        clientName = request.clientName,
                                        isSecretRotated = request.isSecretRotated,
                                    ).then()
                            } else {
                                Mono.error(AccessDeniedException("At least application manager or program-client required."))
                            }
                        }
                }
        }

    override fun getConfigurationFeedback(
        nid: Long,
        aid: Long,
        cid: Long,
        exchange: ServerWebExchange?,
    ): Mono<ConfigurationFeedbackResponseDto> =
        currentPrincipal()
            .flatMap { keycloakPrincipal ->
                accessControlService
                    .hasAccessToConfigurations(
                        keycloakPrincipal = keycloakPrincipal,
                        namespaceId = nid,
                        applicationId = aid,
                    ).flatMap { hasAccess ->
                        if (hasAccess) {
                            configurationFeedbackService
                                .getConfigurationFeedback(
                                    namespaceId = nid,
                                    applicationId = aid,
                                    configurationId = cid,
                                ).map { it.toApi() }
                                .collectList()
                                .map { ConfigurationFeedbackResponseDto(it) }
                        } else {
                            Mono.error(AccessDeniedException("At least application manager or program-client required."))
                        }
                    }
            }

    override fun postConfigurationFeedback(
        nid: Long,
        aid: Long,
        cid: Long,
        configurationFeedbackRequestDto: Mono<ConfigurationFeedbackRequestDto>,
        exchange: ServerWebExchange?,
    ): Mono<Void> =
        configurationFeedbackRequestDto.flatMap { request ->
            currentPrincipal()
                .flatMap { keycloakPrincipal ->
                    accessControlService
                        .hasAccessToConfigurations(
                            keycloakPrincipal = keycloakPrincipal,
                            namespaceId = nid,
                            applicationId = aid,
                        ).flatMap { hasAccess ->
                            if (hasAccess) {
                                configurationFeedbackService
                                    .addConfigurationFeedback(
                                        namespaceId = nid,
                                        applicationId = aid,
                                        configurationId = cid,
                                        clientName = request.clientName,
                                        version = request.appliedVersion,
                                    ).then()
                            } else {
                                Mono.error(AccessDeniedException("At least application manager or program-client required."))
                            }
                        }
                }
        }

    override fun subscribeOnNotificationSse(
        nid: Long,
        aid: Long,
        exchange: ServerWebExchange?,
    ): Flux<NotificationEventDto> =
        currentPrincipal()
            .flatMapMany { keycloakPrincipal ->
                accessControlService
                    .hasAccessToApplication(
                        keycloakPrincipal = keycloakPrincipal,
                        namespaceId = nid,
                        applicationId = aid,
                    ).flatMapMany { hasAccess ->
                        if (hasAccess) {
                            notificationService
                                .subscribeOnNotificationEvents(
                                    namespaceId = nid,
                                    applicationId = aid,
                                ).map { it.toApi() }
                        } else {
                            Flux.error(AccessDeniedException("At least application manager or program-client required."))
                        }
                    }
            }

    private fun currentPrincipal(): Mono<KeycloakPrincipal> =
        ReactiveSecurityContextHolder
            .getContext()
            .map { it.authentication }
            .map { it.details }
            .cast(KeycloakPrincipal::class.java)
}
