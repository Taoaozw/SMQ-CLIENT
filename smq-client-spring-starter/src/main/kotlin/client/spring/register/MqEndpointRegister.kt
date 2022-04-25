package client.spring.register

import client.spring.annotation.*
import client.spring.publisher.*
import org.springframework.beans.factory.*
import org.springframework.beans.factory.annotation.*
import org.springframework.beans.factory.config.*
import org.springframework.beans.factory.support.*
import org.springframework.context.*
import org.springframework.context.annotation.*
import org.springframework.core.env.*
import org.springframework.core.io.*
import org.springframework.core.type.*
import org.springframework.core.type.filter.*
import org.springframework.util.*

class MqEndpointRegister : ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {


    private lateinit var resource: ResourceLoader

    private lateinit var environment: Environment

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        getScanner(environment, resource, registry).apply {
            resourceLoader = resource
            addIncludeFilter(AnnotationTypeFilter(MqEndpoint::class.java))
        }.run {
            getBasePackages(importingClassMetadata).flatMap(::findCandidateComponents)
                .filterIsInstance<AnnotatedBeanDefinition>().forEach {
                    registerMqEndpoint(registry, it.metadata)
                }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun registerMqEndpoint(
        registry: BeanDefinitionRegistry,
        annotationMetaData: AnnotationMetadata
    ) {
        val name = annotationMetaData.className
        val clazz = ClassUtils.resolveClassName(name, null) as Class<Any>
        val factoryBean = MqEndpointFactoryBean(clazz, registry as ConfigurableBeanFactory)
        val definition = BeanDefinitionBuilder.genericBeanDefinition(clazz) { factoryBean.`object` }
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
        definition.setLazyInit(true)
        val beanDefinition = definition.beanDefinition
        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, name)
        beanDefinition.setAttribute("feignClientsRegistrarFactoryBean", factoryBean)
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry)
    }


    override fun setResourceLoader(resourceLoader: ResourceLoader) {
        this.resource = resourceLoader
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }
}