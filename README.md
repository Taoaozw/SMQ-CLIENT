# SMQ-CLIENT
This is a easy way to publish MQTT message and receive MQTT message 

## This is provider a spring stater for quick use

### Recive message form the broker  

`MqRouter` is like a controller  and MqPath is the subscribe topic 

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

### Publish MQTT message to broker

This is very similar to Feign :

```kotlin

@MqEndpoint
interface TestEndpoint {
    @MqPath("hello/{id}")
    fun test(@PathVariable id: Long, @MqEncoder(name = "R001") msg: String)
}


```
