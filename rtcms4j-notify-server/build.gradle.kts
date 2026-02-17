apply {
    plugin("org.openapi.generator")
    plugin("org.springframework.boot")
    plugin("com.google.cloud.tools.jib")
}

val specDependency by configurations.registering {
    isCanBeConsumed = false
    isCanBeResolved = false
}
val spec by configurations.registering {
    extendsFrom(specDependency.get())
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false
}

dependencies {
    api(project(":rtcms4j-notify-api"))

    implementation("ru.enzhine:rtcms4j-core-api")
    specDependency("ru.enzhine:rtcms4j-core-api")

    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

val projectBuildDir = layout.buildDirectory.get()

tasks.openApiGenerate {
    // DOCS: https://openapi-generator.tech/docs/generators/java/
    generatorName = "java"

    outputDir = "$projectBuildDir/generated"
    inputSpec.set(
        spec
            .flatMap { it.elements }
            .map {
                resources.text
                    .fromArchiveEntry(it, "static/openapi/core-api.yaml")
                    .asFile()
                    .absolutePath
            },
    )
    modelPackage = "ru.enzhine.rtcms4j.core.api.dto"
    apiPackage = "ru.enzhine.rtcms4j.core.api"

    configOptions.set(
        mapOf(
            "library" to "webclient",
            "documentationProvider" to "none",
            "openApiNullable" to "false",
            "useJakartaEe" to "true",
        ),
    )
}

tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

tasks.runKtlintCheckOverMainSourceSet {
    enabled = false
}

sourceSets {
    main {
        java {
            srcDir("$projectBuildDir/generated/src/main/java")
        }
    }
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = false
}

tasks.withType<PublishToMavenRepository> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
    testLogging { exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL }
}

jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"
    }

    to {
        image = "ghcr.io/hse-rtcms4j/${project.name.lowercase()}"
        tags =
            setOf(
                project.version.toString(),
                "latest",
            )

        auth {
            username = System.getenv("GITHUB_ACTOR") ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: ""
        }
    }

    setAllowInsecureRegistries(true)
}
