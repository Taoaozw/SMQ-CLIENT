package client.spring.annotation


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class MqCodec(
    val name: String = "",
    val version: String = "0.0.1",
)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class MqDecoder(
    val name: String = "",
    val version: String = "0.0.1",
)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class MqEncoder(
    val name: String = "",
    val version: String = "0.0.1",
)




@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MqSubscriber(
    val topic: String = "",
)