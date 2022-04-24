package client.spring.annotation


/**
 * Listen to messages on a topic,
 * But the subscription operation needs to be triggered separately
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MqRouter(
    val value: String = "",
)


/**
 * Represents a connection to an MQTT broker
 * Messages can be pushed to the broker
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MqEndpoint(
    val value: String = "",
)


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class MqCoderProvider(
    val value: String = "",
)
