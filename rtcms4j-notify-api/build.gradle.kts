import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

apply {
    plugin("org.openapi.generator")
}

dependencies {
    api("io.projectreactor:reactor-core")
    api("org.springframework:spring-web")
    api("org.springframework:spring-context")
    api("org.springframework.data:spring-data-commons")
    api("com.fasterxml.jackson.core:jackson-annotations")
    api("jakarta.validation:jakarta.validation-api")
    api("jakarta.annotation:jakarta.annotation-api")
    api("jakarta.servlet:jakarta.servlet-api")
}

val projectBuildDir = layout.buildDirectory.get()

tasks.openApiGenerate {
    // DOCS: https://openapi-generator.tech/docs/generators/spring/
    generatorName = "spring"

    outputDir = "$projectBuildDir/generated"
    inputSpec = "$projectDir/src/main/resources/static/openapi/notify-api.yaml"
    modelPackage = "ru.enzhine.rtcms4j.notify.api.dto"
    apiPackage = "ru.enzhine.rtcms4j.notify.api"

    configOptions.set(
        mapOf(
            "reactive" to "true",
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useTags" to "true",
            "skipDefaultInterface" to "true",
            "documentationProvider" to "none",
            "useSwaggerUI" to "false",
            "useResponseEntity" to "false",
            "requestMappingMode" to "none",
            "openApiNullable" to "false",
        ),
    )

    openapiGeneratorIgnoreList = listOf("**/ApiUtil.java")
}

tasks.runKtlintCheckOverMainSourceSet {
    enabled = false
}

tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

sourceSets {
    main {
        java {
            srcDir("$projectBuildDir/generated/src/main/java")
        }
    }
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
