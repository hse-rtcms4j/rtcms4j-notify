package ru.enzhine.rtcms4j.notify.client

import org.springframework.cloud.openfeign.FeignClient
import ru.enzhine.rtcms4j.notify.api.NotifyApi

@FeignClient(name = "rtcms4j-notify-client", path = "/api/v1")
interface NotifyClient : NotifyApi
