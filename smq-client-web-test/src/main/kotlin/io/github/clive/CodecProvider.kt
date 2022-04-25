package io.github.clive

import client.spring.annotation.*
import smq.client.core.*


@MqCoderProvider
class CodecProvider {


    @MqDecoder(name = "R001")
    fun decodeR001(msg: SmqPublishMessage): String {
        return msg.payload.toString()
    }

}