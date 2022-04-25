package client.spring.publisher

import client.spring.annotation.*
import client.spring.extension.*
import client.spring.support.*
import org.springframework.beans.factory.*
import java.lang.reflect.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.*


class MqPublisher(
    private val context: BeanFactory,
    private val invokeHandler: PublisherInvokeHandler = PublisherInvokeHandler()
) {


    fun instance(type: KClass<*>) = createProxy(type).apply {
        invokeHandler.target = this
        type.declaredMemberFunctions.filter(KFunction<*>::isPublisher).forEach { func ->
            func.findAnnotation<MqEncoder>()?.let {
                PublisherMethodHandler(func, it.name, context).also { handler -> invokeHandler[func] = handler }
            }
        }
    }



    @Suppress("UNCHECKED_CAST")
    private fun <T> createProxy(clz: KClass<T>): T where T : Any =
        Proxy.newProxyInstance(clz::class.java.classLoader, arrayOf(clz.java), invokeHandler) as T


}


class PublisherInvokeHandler : InvocationHandler {

    lateinit var target: Any

    private var delegate: MutableMap<KFunction<*>, PublisherMethodHandler> = hashMapOf()

    operator fun set(method: KFunction<*>, handler: PublisherMethodHandler) {
        delegate[method] = handler
    }

    operator fun get(method: KFunction<*>): PublisherMethodHandler? = delegate[method]

    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
        val kFunction = method.kotlinFunction!!
        return if (!kFunction.isEqualsFunction()) {
            if (kFunction.isHashCodeFunction()) {
                this.hashCode()
            } else {
                if (kFunction.isToStringFunction()) this.toString()
                else (this.delegate[kFunction] as PublisherMethodHandler).invoke(args)
            }
        } else {
            try {
                return this == args?.let { Proxy.getInvocationHandler(args[0]) }
            } catch (e: IllegalArgumentException) {
                return false
            }
        }
    }

}
