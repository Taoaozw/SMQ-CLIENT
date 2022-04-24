package client.vertx.impl

import io.vertx.core.*
import io.vertx.mqtt.*
import smq.client.core.*



class VertxMqClient(val properties: MqttClientProperties) : MqClient {

    private val client: MqttClient

    init {
        client = MqttClient.create(Vertx.vertx(), MqttClientOptions())
    }

    override fun publish(topic: String, payload: Any, qos: Int, isRetain: Boolean) {
        TODO("Not yet implemented")
    }
}