package ru.enzhine.rtcms4j.notify.service.external

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.core.api.CoreApi
import ru.enzhine.rtcms4j.notify.security.dto.KeycloakPrincipal

@Service
class AccessControlServiceImpl(
    private val coreApi: CoreApi,
) : AccessControlService {
    override fun hasAccessToApplication(
        keycloakPrincipal: KeycloakPrincipal,
        namespaceId: Long,
        applicationId: Long,
    ): Mono<Boolean> =
        if (keycloakPrincipal.isClient) {
            Mono.just(
                keycloakPrincipal.namespaceId == namespaceId &&
                    keycloakPrincipal.applicationId == applicationId,
            )
        } else {
            coreApi
                .hasAccessToApplication(namespaceId, applicationId)
                .thenReturn(true)
        }

    override fun hasAccessToConfigurations(
        keycloakPrincipal: KeycloakPrincipal,
        namespaceId: Long,
        applicationId: Long,
    ): Mono<Boolean> = hasAccessToApplication(keycloakPrincipal, namespaceId, applicationId)
}
