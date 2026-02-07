package ru.enzhine.rtcms4j.notify.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
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

@RestController
@RequestMapping("/api/v1")
class NotifyController : NotifyApi {
    override fun getApplicationFeedback(
        nid: @NotNull Long?,
        aid: @NotNull Long?,
        exchange: ServerWebExchange?,
    ): Mono<ApplicationFeedbackResponseDto?>? {
        TODO("Not yet implemented")
    }

    override fun getConfigurationFeedback(
        nid: @NotNull Long?,
        aid: @NotNull Long?,
        cid: @NotNull Long?,
        exchange: ServerWebExchange?,
    ): Mono<ConfigurationFeedbackResponseDto?>? {
        TODO("Not yet implemented")
    }

    override fun postApplicationFeedback(
        nid: @NotNull Long?,
        aid: @NotNull Long?,
        applicationFeedbackRequestDto: @Valid Mono<ApplicationFeedbackRequestDto?>?,
        exchange: ServerWebExchange?,
    ): Mono<Void?>? {
        TODO("Not yet implemented")
    }

    override fun postConfigurationFeedback(
        nid: @NotNull Long?,
        aid: @NotNull Long?,
        cid: @NotNull Long?,
        configurationFeedbackRequestDto: @Valid Mono<ConfigurationFeedbackRequestDto?>?,
        exchange: ServerWebExchange?,
    ): Mono<Void?>? {
        TODO("Not yet implemented")
    }

    override fun subscribeOnConfigurationSse(
        nid: @NotNull Long?,
        aid: @NotNull Long?,
        cid: @NotNull Long?,
        exchange: ServerWebExchange?,
    ): Flux<NotificationEventDto?>? {
        TODO("Not yet implemented")
    }
}
