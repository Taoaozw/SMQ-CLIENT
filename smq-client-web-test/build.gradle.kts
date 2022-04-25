@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.plugin)
    alias(libs.plugins.spring.dependency)
}

dependencies{
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(project(":smq-client-vert-impl"))
    implementation(project(":smq-client-spring-starter"))
}
