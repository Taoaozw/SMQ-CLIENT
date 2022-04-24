package client.spring.support


import client.spring.annotation.*
import client.spring.exception.*
import client.spring.extension.*
import org.springframework.beans.factory.*
import org.springframework.web.bind.annotation.*
import smq.client.core.*
import kotlin.reflect.*
import kotlin.reflect.full.*

class PublisherMethodHandler(
    private val func: KFunction<*>,
    private val encoderName: String,
    private val context: BeanFactory,
) {

    private val qos: Int

    private val path: String

    private val isRetain: Boolean

    init {
        func.annotations
            .filterIsInstance<MqPath>()
            .first()
            .let {
                isRetain = it.isRetain
                qos = it.qos
                path = it.path
            }
    }


    private val decoder: CodecBundle by lazy {
        requireNotNull(context.getBean<MqCodecMappingHandler>().encoderProvider[encoderName]) {
            "Encoder $encoderName not found"
        }
    }

    private val client: MqClient by lazy {
        requireNotNull(context.getBean()) { "MqClient not found" }
    }

    private val parameter: List<FunctionParameter> = func.valueParameters.map {
        when {
            it.hasAnnotation<PathVariable>() -> VariableParameter(it.name!!)
            it.hasAnnotation<MqEncoder>() -> OriginalParameter(it.name!!)
            else -> DefaultParameter(it.name!!)
        }
    }


    fun invoke(args: Array<Any>?) {
        if (args.isNullOrEmpty()) {
            throw PublishFunctionParameterNullException("Publish function parameter must not be null or empty!")
        } else if (args.size != parameter.size) throw IllegalArgumentException("args size is not equal to function <${func.name}> parameter size")
        else {
            val context = VariableContext()
            var result: Any? = null
            parameter.forEachIndexed { index, any ->
                when (any) {
                    is VariableParameter -> context[any.name] = args[index]
                    is OriginalParameter -> {
                        result = decoder.second.call(decoder.first, args[index])
                    }
                    is DefaultParameter -> result = args[index]
                }
            }
            client.publish(SimpleSpe.parse(path, context), result!!, qos, false)
        }
    }


}

sealed interface FunctionParameter {
    val name: String
}

@JvmInline
value class VariableParameter(override val name: String) : FunctionParameter

@JvmInline
value class OriginalParameter(override val name: String) : FunctionParameter

@JvmInline
value class DefaultParameter(override val name: String) : FunctionParameter