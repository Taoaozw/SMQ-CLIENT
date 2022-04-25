package client.vertx.impl

import org.springframework.boot.context.properties.*
import org.springframework.context.*
import org.springframework.context.annotation.*


@Configuration
@EnableConfigurationProperties(MqttClientProperties::class)
class SmqClientConfiguration {


    @Bean
    fun mqttClient(properties: MqttClientProperties, publisher: ApplicationEventPublisher): VertxMqClient =
        VertxMqClient(properties, publisher)

}