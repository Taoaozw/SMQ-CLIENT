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
    private val bridgeMethod: Method = BridgeMethodResolver.findBridgedMethod(method),
    private val params: List<MethodParameter> = bridgeMethod.parameters.mapIndexed { index, param ->
        MethodParameter(index, param, bridgeMethod)
    }
) {

    init {
        ReflectionUtils.makeAccessible(bridgeMethod)
    }

    fun invoke(context: ApplicationContext, args: Array<Any>) {
        context.getBean(beanClass).let {
            bridgeMethod.invoke(it, *args)
        }
    }


    fun args(
        msg: SmqPublishMessage,
        pathVariableMap: MutableMap<String, String>?,
        decoders: Map<String, CodecBundle>,
    ): Array<Any> {
        val args = arrayListOf<Any>()
        params.forEachIndexed { _, param ->
            if (param.isPathVariable) {
                val pathVariableName = param.pathVariableName()
                val pathArg = (pathVariableMap?.get(pathVariableName)?.let {
                    Convertor.convertPath(it, param.parameter.type)
                } ?: throw NotFoundPathVariableException("$pathVariableName is not found value in ${msg.topic} "))
                args.add(pathArg)
            } else {
                if (param.isNeedProtocolParse) {
                    param.parameter.getAnnotation(MqDecoder::class.java).let {
                        decoders[it.name]?.let { f ->
                            f.second.call(f.first, msg)?.let { result -> args.add(result) }
                        } ?: throw NotFoundMqttProtocolException("Not found decoder name with  <${it.name}> !")
                    }
                    //寻找 mqtt协议参数
                } else {
                    args.add(msg)
                }
            }
        }
        return args.toArray()
    }
}

data class MethodParameter(
    val index: Int,
    val parameter: Parameter,
    // method or constructor
    val executable: Executable,
    val isPathVariable: Boolean = parameter.isAnnotationPresent(PathVariable::class.java),
    val isNeedProtocolParse: Boolean = parameter.isAnnotationPresent(MqDecoder::class.java),
) {

    fun pathVariableName(): String {
        parameter.getAnnotation(PathVariable::class.java)?.let {
            if (it.value.isEmpty()) {
                return parameter.name
            } else {
                return it.value
            }
        }
        return parameter.getAnnotation(PathVariable::class.java)?.value ?: parameter.name
    }

}
