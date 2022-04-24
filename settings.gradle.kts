rootProject.name = "smq-client"
include("smq-client-spring-starter")
include("smq-client-vert-impl")
include("smq-client-web-test")
include("smq-client-core")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("vertx", "4.2.6")
            version("kotlin", "1.6.20")
            version("kotlin-logging", "2+")
            version("spring", "2.6.+")
            version("coroutines", "1.6.+")

            version("kotest", "5.+")
            version("kotest-ext-spring", "1.1.+")
            version("junit", "5.+")

            library("kt-reflect", "org.jetbrains.kotlin", "kotlin-reflect").withoutVersion()

            library("vertx-core", "io.vertx", "vertx-core").versionRef("vertx")
            library("vertx-mqtt", "io.vertx", "vertx-mqtt").versionRef("vertx")
            library("vertx-kt", "io.vertx", "vertx-lang-kotlin").versionRef("vertx")
            library("vertx-kt-coroutines", "io.vertx", "vertx-lang-kotlin-coroutines").versionRef("vertx")

            library("spring-test", "org.springframework.boot", "spring-boot-starter-test").withoutVersion()
            library("spring-context", "org.springframework", "spring-context").withoutVersion()
            library("kotest-spring", "io.kotest.extensions", "kotest-extensions-spring").versionRef("kotest-ext-spring")
            library("kotest-assertions", "io.kotest", "kotest-assertions-core").versionRef("kotest")
            library("kotest-junit5", "io.kotest", "kotest-runner-junit5").versionRef("kotest")
            library("kotest-property", "io.kotest", "kotest-property").versionRef("kotest")
            library("junit-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit-enginer", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")



            library("kt-log", "io.github.microutils", "kotlin-logging-jvm").versionRef("kotlin-logging")

            library(
                "coroutines-core-jvm",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-core-jvm"
            ).versionRef("coroutines")
            library("coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines")
            library("coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef("coroutines")

            //spring
            library("spring-boot-starter-web", "org.springframework.boot", "spring-boot-starter-web").withoutVersion()
            library("spring-boot-starter", "org.springframework.boot", "spring-boot-starter").withoutVersion()
            library("spring-boot-starter-test", "org.springframework.boot", "spring-boot-starter-test").withoutVersion()
            library("spring-web", "org.springframework", "spring-web").withoutVersion()

            bundle(
                "test",
                listOf(
                    "kotest-junit5", "kotest-assertions", "kotest-property", "junit-enginer", "coroutines-core",
                    "coroutines-core-jvm", "coroutines-test"
                )
            )
            bundle(
                "spring-test", listOf(
                    "junit-api", "kotest-spring", "kotest-junit5", "kotest-assertions", "kotest-property",
                    "junit-enginer", "spring-test", "coroutines-core", "coroutines-core-jvm", "coroutines-test"
                )
            )


            plugin("kotlin-spring", "org.jetbrains.kotlin.plugin.spring").versionRef("kotlin")
            plugin("kotlin-plugin-allopen", "org.jetbrains.kotlin.plugin.allopen").versionRef("kotlin")
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-kapt", "org.jetbrains.kotlin.kapt").versionRef("kotlin")
            plugin("spring", "org.springframework.boot").versionRef("spring")
            plugin("spring-plugin", "org.jetbrains.kotlin.plugin.spring").versionRef("kotlin")
            plugin("spring.dependency", "io.spring.dependency-management").version("latest.release")

        }
    }
}
