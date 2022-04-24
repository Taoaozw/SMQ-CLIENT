package smq.client.core


/**
 * SMQ Client
 * provider extension for self-used <MQTT-CLIENT>
 */
interface MqClient {

    fun publish(topic: String, payload: Any, qos: Int = 0, isRetain: Boolean = false)

}