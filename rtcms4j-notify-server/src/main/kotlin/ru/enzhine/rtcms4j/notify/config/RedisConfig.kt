package ru.enzhine.rtcms4j.notify.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import ru.enzhine.rtcms4j.notify.repository.dto.ApplicationFeedback
import ru.enzhine.rtcms4j.notify.repository.dto.ConfigurationFeedback

@Configuration
class RedisConfig {
    @Bean
    fun redisTemplateApplicationFeedback(
        objectMapper: ObjectMapper,
        redisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, ApplicationFeedback> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = Jackson2JsonRedisSerializer(objectMapper, ApplicationFeedback::class.java)

        val context =
            RedisSerializationContext
                .newSerializationContext<String, ApplicationFeedback>()
                .key(keySerializer)
                .value(valueSerializer)
                .hashKey(keySerializer)
                .hashValue(valueSerializer)
                .build()

        return ReactiveRedisTemplate(redisConnectionFactory, context)
    }

    @Bean
    fun redisTemplateConfigurationFeedback(
        objectMapper: ObjectMapper,
        redisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, ConfigurationFeedback> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = Jackson2JsonRedisSerializer(objectMapper, ConfigurationFeedback::class.java)

        val context =
            RedisSerializationContext
                .newSerializationContext<String, ConfigurationFeedback>()
                .key(keySerializer)
                .value(valueSerializer)
                .hashKey(keySerializer)
                .hashValue(valueSerializer)
                .build()

        return ReactiveRedisTemplate(redisConnectionFactory, context)
    }

    @Bean
    fun reactiveRedisMessageListenerContainer(
        redisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisMessageListenerContainer = ReactiveRedisMessageListenerContainer(redisConnectionFactory)
}
