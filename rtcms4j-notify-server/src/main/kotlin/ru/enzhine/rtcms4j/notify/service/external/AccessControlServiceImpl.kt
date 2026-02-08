package ru.enzhine.rtcms4j.notify.service.external

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.core.api.CoreApi
import ru.enzhine.rtcms4j.notify.config.props.KeycloakProperties
import ru.enzhine.rtcms4j.notify.security.dto.KeycloakPrincipal

@Service
class AccessControlServiceImpl(
    private val coreApi: CoreApi,
    private val keycloakProperties: KeycloakProperties,
) : AccessControlService {
    companion object {
        const val ATTRIBUTE_KEY_NAMESPACE_ID = "NAMESPACE_ID"
        const val ATTRIBUTE_KEY_APPLICATION_ID = "APPLICATION_ID"
    }

    override fun hasAccessToApplication(
        keycloakPrincipal: KeycloakPrincipal,
        namespaceId: Long,
        applicationId: Long,
    ): Mono<Boolean> =
        if (keycloakPrincipal.isClient) {
            Mono.just(
                keycloakPrincipal.attributes[keycloakProperties.resourcePrefix + ATTRIBUTE_KEY_NAMESPACE_ID] ==
                    namespaceId.toString() &&
                    keycloakPrincipal.attributes[keycloakProperties.resourcePrefix + ATTRIBUTE_KEY_APPLICATION_ID] ==
                    applicationId.toString(),
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
