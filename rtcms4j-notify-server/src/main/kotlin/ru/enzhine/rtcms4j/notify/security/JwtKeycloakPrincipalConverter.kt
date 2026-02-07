package ru.enzhine.rtcms4j.notify.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import reactor.core.publisher.Mono
import ru.enzhine.rtcms4j.notify.security.dto.KeycloakPrincipal
import java.util.UUID

class JwtKeycloakPrincipalConverter : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    override fun convert(jwt: Jwt): Mono<AbstractAuthenticationToken>? {
        val authorities = extractAuthorities(jwt)
        val attributes = extractAttributes(jwt)

        val principal =
            KeycloakPrincipal(
                sub = UUID.fromString(jwt.subject),
                username = jwt.getClaimAsString("preferred_username"),
                clientId = jwt.getClaimAsString("azp"),
                roles = authorities.map { it.authority }.toSet(),
                attributes = attributes,
            )

        val token =
            JwtAuthenticationToken(
                jwt,
                authorities,
                principal.username ?: principal.clientId ?: principal.sub.toString(),
            ).apply {
                details = principal
            }

        return Mono.just(token)
    }

    private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
        val roles = mutableSetOf<String>()

        // realm roles
        val realmAccess = jwt.claims["realm_access"] as? Map<*, *>
        val realmRoles = realmAccess?.get("roles") as? Collection<*>
        realmRoles?.forEach { roles.add(it.toString()) }

        // client roles
        val resourceAccess = jwt.claims["resource_access"] as? Map<*, *>
        resourceAccess
            ?.values
            ?.mapNotNull { it as? Map<*, *> }
            ?.mapNotNull { it["roles"] as? Collection<*> }
            ?.flatten()
            ?.forEach { roles.add(it.toString()) }

        return roles.map { SimpleGrantedAuthority(it) }
    }

    private fun extractAttributes(jwt: Jwt): Map<String, String> {
        return emptyMap() // TODO: parse jwt attributes
    }
}
