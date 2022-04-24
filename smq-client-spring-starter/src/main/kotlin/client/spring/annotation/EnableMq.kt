package client.spring.annotation

import client.spring.register.*
import org.springframework.context.annotation.*


/**
 * Enable Mq annotation.
 *
 * if you want to use Mq, you should add this annotation to your spring application class.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(value = [MqRouterRegistrar::class, MqEndpointRegister::class])
annotation class EnableMq(

    val value: Array<String> = [""],

    /**
     * like [ComponentScan.basePackages] to scan [MqRouter] and [MqEndpoint] bean.
     */
    val basePackages: Array<String> = [""],

    /**
     *  like [basePackages]
     */
    val basePackageClasses: Array<String> = [""],
)