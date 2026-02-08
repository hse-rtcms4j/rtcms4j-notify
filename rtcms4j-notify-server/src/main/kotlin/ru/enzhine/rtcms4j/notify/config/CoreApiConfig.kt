package ru.enzhine.rtcms4j.notify.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import ru.enzhine.rtcms4j.core.ApiClient
import ru.enzhine.rtcms4j.core.api.CoreApi
import ru.enzhine.rtcms4j.notify.config.props.CoreApiProperties

@Configuration
class CoreApiConfig {
    @Bean
    fun coreApi(coreApiProperties: CoreApiProperties) =
        CoreApi().apply {
            val mutatedWebClient =
                apiClient.webClient
                    .mutate()
                    .baseUrl(coreApiProperties.baseUrl)
                    .filter(jwtInterceptor())
                    .build()

            apiClient = ApiClient(mutatedWebClient)
        }

    private fun jwtInterceptor() =
        ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
            getCurrentJwtToken()
                .map {
                    ClientRequest
                        .from(clientRequest)
                        .header("Authorization", "Bearer $it")
                        .build()
                }
        }

    private fun getCurrentJwtToken() =
        ReactiveSecurityContextHolder
            .getContext()
            .map { it.authentication }
            .map { it.credentials }
            .cast(String::class.java)
}
