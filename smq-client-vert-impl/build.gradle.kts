@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.plugin)
    alias(libs.plugins.spring.dependency)
}


dependencies{
    implementation(libs.vertx.mqtt)
    implementation(libs.spring.boot.starter)
    implementation(project(":smq-client-core"))
}