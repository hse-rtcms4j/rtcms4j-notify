package ru.enzhine.rtcms4j.notify.service.external

import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.security.dto.KeycloakPrincipal

interface AccessControlService {
    fun hasAccessToApplication(
        keycloakPrincipal: KeycloakPrincipal,
        namespaceId: Long,
        applicationId: Long,
    ): Mono<Boolean>

    fun hasAccessToConfigurations(
        keycloakPrincipal: KeycloakPrincipal,
        namespaceId: Long,
        applicationId: Long,
    ): Mono<Boolean>
}
