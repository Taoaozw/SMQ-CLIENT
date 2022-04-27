# SMQ-CLIENT

This is a easy way to publish MQTT message and receive MQTT message

## This is provider a spring stater for quick use

### Recive message form the broker

`MqRouter` is like a controller and MqPath is the subscribe topic

it also provider parse the @PathVariable

```kotlin
@MqRouter
class TestController(val client: VertxMqClient, private val endpoint: TestEndpoint) : CommandLineRunner {


    @MqPath("test/{id}")
    fun test(@PathVariable id: Long, @MqDecoder(name = "R001") msg: String) {
        println("id :$id msg: $msg")
    }

    override fun run(vararg args: String?) {
        endpoint.test(2, "hello")
    }


}

```

### Protocol provider

```kotlin
@MqCoderProvider
class CodecProvider {


    @MqDecoder(name = "R001")
    fun decodeR001(msg: SmqPublishMessage): String = msg.payload.toString()


    @MqEncoder(name = "R001")
    fun encodeR001(msg: String): Buffer = Json.obj("msg" to "hello").toBuffer()


}


```

### Publish MQTT message to broker

This is very similar to Feign :

```kotlin

@MqEndpoint
interface TestEndpoint {
    @MqPath("hello/{id}")
    fun test(@PathVariable id: Long, @MqEncoder(name = "R001") msg: String)
}



```

## Next stage goal

- [ ] Optimized reflection code via KT Reflection

- [ ] Added switch for system event ($SYS) listening method provision

- [ ] Extend MQ Path by providing subscription and receive capabilities

- [ ] Provides the suspended message publishing and listening methods

- [ ] Added configuration items: will message and user authentication parts

- [ ] Add the dynamic loading function of protocol to make use of SPI mechanism
