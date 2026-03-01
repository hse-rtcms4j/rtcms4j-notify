package ru.enzhine.rtcms4j.notify.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import ru.enzhine.rtcms4j.notify.security.JwtKeycloakPrincipalConverter

@Configuration
class SecurityConfig {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeExchange { auth ->
                auth
                    .pathMatchers("/actuator/health/**")
                    .permitAll()
                    .anyExchange()
                    .authenticated()
            }.oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(JwtKeycloakPrincipalConverter())
                }
            }

        return http.build()
    }
}
