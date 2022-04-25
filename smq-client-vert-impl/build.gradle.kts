
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("kapt")
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.plugin)
    alias(libs.plugins.spring.dependency)
}


dependencies{
    kapt(libs.spring.configuration)
    api(project(":smq-client-core"))

    implementation(libs.vertx.mqtt)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.configuration)
}

kapt {
    arguments {
        arg(
            "org.springframework.boot.configurationprocessor.additionalMetadataLocations",
            *tasks.processResources.get().outputs.files.files.map { it.absolutePath }.toTypedArray()
        )
    }
}

tasks.bootJar{
    enabled = false
}

tasks.jar{
    enabled = true
}