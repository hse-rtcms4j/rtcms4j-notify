package ru.enzhine.rtcms4j.notify.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import ru.enzhine.rtcms4j.notify.config.props.CorsProperties
import ru.enzhine.rtcms4j.notify.security.JwtKeycloakPrincipalConverter

@Configuration
class SecurityConfig {
    @Bean
    @Primary
    fun corsConfiguration(corsProperties: CorsProperties): CorsConfigurationSource {
        val config =
            CorsConfiguration()
                .apply {
                    allowedOrigins = corsProperties.allowedOrigins
                    allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                    allowedHeaders = listOf("Authorization", "Content-Type")
                    allowCredentials = true
                }

        return UrlBasedCorsConfigurationSource()
            .apply {
                registerCorsConfiguration("/**", config)
            }
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        configurationSource: CorsConfigurationSource,
    ): SecurityWebFilterChain {
        http
            .cors { it.configurationSource(configurationSource) }
            .csrf { it.disable() }
            .authorizeExchange { auth ->
                auth
                    .pathMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .pathMatchers("/actuator/health")
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
