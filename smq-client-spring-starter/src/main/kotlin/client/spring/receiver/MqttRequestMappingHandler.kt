package client.spring.receiver

import client.spring.annotation.*
import client.spring.support.*
import org.springframework.aop.support.*
import org.springframework.beans.factory.*
import org.springframework.context.*
import org.springframework.core.*
import org.springframework.core.annotation.*
import org.springframework.util.*
import java.lang.reflect.*


/**
 *
 * refer:InvocableHandlerMethod
 *
 * AbstractHandlerMethodMapping
 *
 */
class MqttRequestMappingHandler(
    private val codec: MqCodecMappingHandler,
    private val mappingCache: RequestMappingCache
) : InitializingBean, ApplicationContextAware {

    private lateinit var context: ApplicationContext

    private fun initHandlerMethod() = getCandidateBeanNames().filter(filterScopedTarget).forEach(::processCandidateBean)

    private val filterScopedTarget: (String) -> Boolean = { !it.startsWith("scopedTarget.") }

    private val isHandler: (Class<*>) -> Boolean = { AnnotatedElementUtils.hasAnnotation(it, MqRouter::class.java) }

    private fun getCandidateBeanNames() = context.getBeanNamesForType<Any>()

    private fun processCandidateBean(beanName: String) {
        context.getType(beanName)?.takeIf(isHandler)?.let { detectHandlerMethods(it) }
    }

    /**
     * handler is a class that has @MqttController annotation
     */
    private fun detectHandlerMethods(handlerType: Class<*>) {
        // 获取原始类型
        ClassUtils.getUserClass(handlerType).apply {
            // 获取所有符合handler类型的方法
            MethodIntrospector.selectMethods(
                this,
                MethodIntrospector.MetadataLookup { getMappingForMethod(it, handlerType) }
            ).forEach { (m, info) ->
                info?.let {
                    mappingCache.register(
                        it,
                        this,
                        AopUtils.selectInvocableMethod(m, this),
                        codec.decoderProvider
                    )
                }
            }
        }
    }

    /**
     * seek for @MqttRequestMapping annotation in methods and controller class
     */
    private fun getMappingForMethod(method: Method, handlerType: Class<*>): PathMappingInfo? {
        val methodInfo = createRequestMappingInfo(method)
        val controllerInfo = createRequestMappingInfo(handlerType)
        return if (methodInfo != null && controllerInfo != null) {
            controllerInfo.combine(methodInfo)
        } else {
            methodInfo ?: controllerInfo
        }
    }

    private fun createRequestMappingInfo(ele: AnnotatedElement): PathMappingInfo? {
        return ele.getDeclaredAnnotation(MqPath::class.java)?.let { PathMappingInfo(it.path) }
    }

    override fun afterPropertiesSet() {
        initHandlerMethod()
    }


    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }
}
