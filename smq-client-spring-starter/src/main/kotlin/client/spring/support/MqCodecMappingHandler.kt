package client.spring.support

import client.spring.annotation.*
import client.spring.extension.*
import org.springframework.beans.factory.*
import org.springframework.context.*
import java.lang.invoke.MethodHandles
import kotlin.reflect.*
import kotlin.reflect.full.*


typealias CodecBundle = Pair<Any, KFunction<*>>

class MqCodecMappingHandler(context: ApplicationContext) {

    val encoderProvider: Map<String, CodecBundle>

    val decoderProvider: Map<String, CodecBundle>

    init {

        context.getBeansWithAnnotation<MqCoderProvider>().values.flatMap { provider ->
            provider::class.declaredMemberFunctions.mapNotNull { processEncoder(it, provider) }
        }.toMap().also { encoderProvider = it }

        context.getBeansWithAnnotation<MqCoderProvider>().values.flatMap { provider ->
            provider::class.declaredMemberFunctions.mapNotNull { processDecoder(it, provider) }
        }.toMap().also { decoderProvider = it }

    }


    private fun processEncoder(func: KFunction<*>, provider: Any): Pair<String, CodecBundle>? =
        func.findAnnotation<MqEncoder>()?.takeIf { func.checkEncoder() }?.let { it.name to (provider to func) }


    private fun processDecoder(func: KFunction<*>, provider: Any): Pair<String, CodecBundle>? =
        func.findAnnotation<MqDecoder>()?.takeIf { func.checkDecoder() }?.let { it.name to (provider to func) }

}

