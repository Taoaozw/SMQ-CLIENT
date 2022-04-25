package client.vertx.impl

import io.netty.handler.codec.mqtt.*
import io.vertx.core.*
import io.vertx.core.buffer.*
import io.vertx.core.http.impl.HttpClientConnection.*
import io.vertx.mqtt.*
import org.springframework.context.*
import smq.client.core.*


class VertxMqClient(private val properties: MqttClientProperties, private val publisher: ApplicationEventPublisher) :
    MqClient {

    val cli: MqttClient


    fun subs(topic: String) {
        cli.subscribe(topic, 0)
    }

    init {
        val options = MqttClientOptions().apply {
            clientId = properties.clientId
            username = properties.username
            password = properties.password
            isCleanSession = properties.cleanSession
        }
        cli = MqttClient.create(Vertx.vertx(), options)

        cli.publishHandler {
            publisher.publishEvent(PublisherEvent(it.topicName(), it.payload()))
        }

        cli.closeHandler {
            log.error("client close hook")
        }

        cli.exceptionHandler {
            log.error("client exception handler", it)
            connectToBroker()
        }
        connectToBroker()
    }

    private fun connectToBroker() {
        cli.connect(properties.port, properties.host) {
            if (it.succeeded()) {
                log.info("Connected to ${properties.host}:${properties.port}")
            } else {
                log.error("Failed to connect to ${properties.host}:${properties.port}")
            }
        }
    }

    override fun publish(topic: String, payload: Any, qos: Int, isRetain: Boolean) {
        cli.publish(topic, payload as Buffer, MqttQoS.valueOf(qos), false, isRetain)
    }

}


data class PublisherEvent(override val topic: String, override val payload: Any) : SmqPublishMessage