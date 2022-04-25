package smq.client.core

interface MqttProperties {

    val port: Int

    val host: String

    val clientId: String?

    val username: String?

    val password: String?

    val cleanSession: Boolean

}