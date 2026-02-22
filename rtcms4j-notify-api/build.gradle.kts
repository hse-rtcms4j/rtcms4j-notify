import java.time.LocalDateTime

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

sourceSets {
    main {
        java {
            srcDir("$projectBuildDir/generated/src/main/java")
        }
    }
}

plugins.withId("com.vanniktech.maven.publish") {
    afterEvaluate {
        tasks
            .findByName("sourcesJar")
            ?.dependsOn("openApiGenerate")
    }
}

tasks {
    runKtlintCheckOverMainSourceSet {
        enabled = false
    }

    compileKotlin {
        dependsOn(openApiGenerate)
    }

    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
}

val groupId: String by rootProject

val versionIdNumber: String by rootProject
val versionIdStatus: String by rootProject
val versionId: String = if (versionIdStatus.isEmpty()) versionIdNumber else "$versionIdNumber-$versionIdStatus"

mavenPublishing {
    coordinates(groupId, project.name, versionId)

    pom {
        name.set(rootProject.name)
        description.set(rootProject.description)
        inceptionYear.set(LocalDateTime.now().year.toString())
        url.set("https://github.com/hse-rtcms4j/rtcms4j-notify/actions")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("Enzhine")
                name.set("Onar")
                url.set("https://github.com/enzhine/")
            }
        }
        scm {
            url.set("https://github.com/hse-rtcms4j/rtcms4j-notify")
            connection.set("scm:git:git://github.com/hse-rtcms4j/rtcms4j-notify.git")
            developerConnection.set("scm:git:ssh://git@github.com/hse-rtcms4j/rtcms4j-notify.git")
        }
    }
}
