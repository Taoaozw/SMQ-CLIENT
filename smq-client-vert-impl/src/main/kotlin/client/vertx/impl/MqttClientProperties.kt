package client.vertx.impl

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "vertx.mqtt")
class MqttClientProperties {

    private var port: Int = 0

    private var host: String? = null

    private var username: String? = null

    private var password: String? = null

    private var clientId: String? = null

}