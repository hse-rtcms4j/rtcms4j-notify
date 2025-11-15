package ru.enzhine.rtcms4j.notify.repository.dto

import java.time.OffsetDateTime

fun newConfigurationSyncStateEntity(
    configurationId: Long,
    sourceIdentity: String,
    commitHash: String?,
    isOnline: Boolean?,
) = ConfigurationSyncStateEntity(
    id = 0L,
    createdAt = OffsetDateTime.MIN,
    configurationId = configurationId,
    sourceIdentity = sourceIdentity,
    commitHash = commitHash,
    isOnline = isOnline,
)
