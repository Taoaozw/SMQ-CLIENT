package io.github.clive

import client.spring.annotation.*
import client.vertx.impl.*
import org.springframework.boot.*
import org.springframework.web.bind.annotation.*
import javax.annotation.*

@MqRouter
class TestController(val client: VertxMqClient) : CommandLineRunner {


    @PostConstruct
    fun init() {

    }

    @MqPath("test/{id}")
    fun test(@PathVariable id: Long, @MqDecoder(name = "R001") msg: String) {
        println("id :$id msg: $msg")
    }

    override fun run(vararg args: String?) {
        client.subs("test/2")
    }


}