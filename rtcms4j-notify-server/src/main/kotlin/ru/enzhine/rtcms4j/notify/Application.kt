package ru.enzhine.rtcms4j.notify

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import ru.enzhine.rtcms4j.notify.config.props.Anchor

@ConfigurationPropertiesScan(basePackageClasses = [Anchor::class])
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
