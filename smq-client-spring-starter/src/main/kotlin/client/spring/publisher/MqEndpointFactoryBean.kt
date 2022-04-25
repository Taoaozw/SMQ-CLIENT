package client.spring.publisher

import org.springframework.beans.factory.*
import org.springframework.context.*

class MqEndpointFactoryBean(
    private val beanType: Class<*>,
    private val beanFactory: BeanFactory
) : FactoryBean<Any>, ApplicationContextAware {
    private lateinit var context: ApplicationContext

    override fun setApplicationContext(ac: ApplicationContext) {
        context = ac
    }

    private fun instance(): Any = MqPublisher(beanFactory).instance(beanType.kotlin)

    override fun isSingleton(): Boolean = true

    override fun getObject(): Any = instance()

    override fun getObjectType(): Class<*> = beanType


}