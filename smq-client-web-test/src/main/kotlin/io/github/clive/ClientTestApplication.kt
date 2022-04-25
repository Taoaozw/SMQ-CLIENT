package io.github.clive

import client.spring.annotation.*
import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@EnableMq
@SpringBootApplication
class ClientTestApplication {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ClientTestApplication::class.java, *args)
        }
    }
}