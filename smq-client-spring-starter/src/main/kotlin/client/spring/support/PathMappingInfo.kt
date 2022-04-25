package client.spring.support

import client.spring.annotation.*
import client.spring.exception.*
import client.spring.extension.*
import org.springframework.context.*
import org.springframework.core.*
import org.springframework.util.*
import org.springframework.web.bind.annotation.*
import smq.client.core.*
import java.lang.reflect.*

class PathMappingInfo(
    val name: String = "",
) {
    fun combine(other: PathMappingInfo): PathMappingInfo = PathMappingInfo(name + other.name)
}


class MethodHandler(
    val path: String,
    private val method: Method,
    private val beanClass: Class<*>,
    decoders: Map<String, CodecBundle>,
    private val bridgeMethod: Method = BridgeMethodResolver.findBridgedMethod(method),
    private val params: List<MethodParam> = bridgeMethod.parameters.map {
        when {
            it.isAnnotationPresent(PathVariable::class.java) -> PathMethodParam(it)
            it.isAnnotationPresent(MqDecoder::class.java) -> DecodeMethodParam(
                it,
                decoders[it.getAnnotation(MqDecoder::class.java).name]
                    ?: throw NotFoundMqttProtocolException(it.getAnnotation(MqDecoder::class.java).name)
            )
            else -> DefaultMethodParam(it)
        }
    }
) {

    init {
        ReflectionUtils.makeAccessible(bridgeMethod)
    }


    fun args(msg: SmqPublishMessage, pathVariableMap: MutableMap<String, String>?): Array<Any> =
        params.map { p -> p.resolve(msg, pathVariableMap) }.toTypedArray()

    fun invoke(context: ApplicationContext, args: Array<Any>): Any? =
        context.getBean(beanClass).let { bridgeMethod.invoke(it, *args) }


}

sealed interface MethodParam {

    val parameter: Parameter
    fun resolve(msg: SmqPublishMessage, pathVariableMap: MutableMap<String, String>?): Any
}

class PathMethodParam(
    override val parameter: Parameter,
    private val pathVariableName: String = parameter.getAnnotation(PathVariable::class.java)!!
        .takeUnless { it.value.isEmpty() }?.value ?: parameter.name,
) : MethodParam {
    override fun resolve(msg: SmqPublishMessage, pathVariableMap: MutableMap<String, String>?) =
        (pathVariableMap?.get(pathVariableName)?.let { Convertor.convertPath(it, parameter.type) }
            ?: throw NotFoundPathVariableException("$pathVariableName is not found value in ${msg.topic} "))

}

class DecodeMethodParam(override val parameter: Parameter, private val decoder: CodecBundle) : MethodParam {
    override fun resolve(msg: SmqPublishMessage, pathVariableMap: MutableMap<String, String>?): Any =
        decoder.second.call(decoder.first, msg)
            ?: throw MqttProtocolDecodeException("Resolve msg use decoder ${decoder.first.javaClass.name} but decode failed !!")

}

class DefaultMethodParam(override val parameter: Parameter) : MethodParam {
    override fun resolve(msg: SmqPublishMessage, pathVariableMap: MutableMap<String, String>?) = msg
}
