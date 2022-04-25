package client.vertx.impl

import org.springframework.boot.context.properties.*
import smq.client.core.*


@ConstructorBinding
@ConfigurationProperties(prefix = "vertx.mqtt")
data class MqttClientProperties(

    override val port: Int,

    override val host: String,

    override val username: String? = null,

    override val password: String? = null,

    override val clientId: String? = null,

    override val cleanSession: Boolean = true,

    ) : MqttProperties