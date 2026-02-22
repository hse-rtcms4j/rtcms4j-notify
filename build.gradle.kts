import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    id("org.springframework.boot") apply false
    id("org.jlleitschuh.gradle.ktlint") apply false
    id("org.openapi.generator") apply false
    id("com.google.cloud.tools.jib") apply false
    id("io.spring.dependency-management")
    id("com.vanniktech.maven.publish")
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("io.spring.dependency-management")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.openapi.generator")
        plugin("org.springframework.boot")
        plugin("com.google.cloud.tools.jib")
        plugin("com.vanniktech.maven.publish")
    }

    val groupId: String by project
    val versionIdNumber: String by project
    val versionIdStatus: String by project

    group = groupId
    val versionId: String = if (versionIdStatus.isEmpty()) versionIdNumber else "$versionIdNumber-$versionIdStatus"
    version = versionId

    dependencyManagement {
        imports {
            val springBootVersion: String by project
            val embeddedPostgresBinariesBomVersion: String by project

            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            mavenBom("io.zonky.test.postgres:embedded-postgres-binaries-bom:$embeddedPostgresBinariesBomVersion")
        }

        dependencies {
            val cucumberVersion: String by project
            dependency("io.cucumber:cucumber-jvm:$cucumberVersion")
            dependency("io.cucumber:cucumber-spring:$cucumberVersion")
            dependency("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")

            val rtcms4jCore: String by project
            dependency("ru.enzhine:rtcms4j-core-api:$rtcms4jCore")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
