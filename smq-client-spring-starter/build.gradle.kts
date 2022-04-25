@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.plugin)
    alias(libs.plugins.spring.dependency)
}

dependencies {
    api(rootProject.libs.kt.log)
    api(rootProject.libs.kt.reflect)
    api(rootProject.libs.spring.web)
    api(rootProject.libs.spring.boot.starter)
    implementation(project(":smq-client-core"))
    testImplementation(rootProject.libs.bundles.test)
}