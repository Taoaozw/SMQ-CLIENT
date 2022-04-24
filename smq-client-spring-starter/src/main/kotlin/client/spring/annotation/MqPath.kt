package client.spring.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MqPath(

    val path: String,

    val value: String = "",

    val encoder: String = "",

    val decoder: String = "",

    val isRetain: Boolean = false,

    val qos: Int = 0,
)





