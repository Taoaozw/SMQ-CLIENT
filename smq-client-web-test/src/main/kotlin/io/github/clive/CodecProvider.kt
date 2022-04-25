package io.github.clive

import client.spring.annotation.*
import io.vertx.core.buffer.*
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.*
import smq.client.core.*


@MqCoderProvider
class CodecProvider {


    @MqDecoder(name = "R001")
    fun decodeR001(msg: SmqPublishMessage): String = msg.payload.toString()


    @MqEncoder(name = "R001")
    fun encodeR001(msg: String): Buffer = Json.obj("msg" to "hello").toBuffer()


}

fun main() {
    val obj = Json.obj("msg" to "hello")
    println(obj.toString())
}
