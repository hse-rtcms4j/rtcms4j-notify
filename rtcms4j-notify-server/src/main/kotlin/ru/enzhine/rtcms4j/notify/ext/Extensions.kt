package ru.enzhine.rtcms4j.notify.ext

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toEpochMillis() = this.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
