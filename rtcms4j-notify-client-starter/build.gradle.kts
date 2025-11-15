dependencies {
    api(project(":rtcms4j-notify-client"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
