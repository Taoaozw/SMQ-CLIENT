package client.spring.register

import client.spring.annotation.*
import org.springframework.beans.factory.support.*
import org.springframework.context.*
import org.springframework.context.annotation.*
import org.springframework.core.env.*
import org.springframework.core.io.*
import org.springframework.core.type.*
import org.springframework.core.type.filter.*

class MqRouterRegistrar : ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private lateinit var resource: ResourceLoader

    private lateinit var environment: Environment

    override fun setResourceLoader(resourceLoader: ResourceLoader) {
        this.resource = resourceLoader
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        getScanner(environment,resource, registry).apply {
            resourceLoader = resource
            addIncludeFilter(AnnotationTypeFilter(MqRouter::class.java))
            addIncludeFilter(AnnotationTypeFilter(MqCoderProvider::class.java))
        }.run {
            scan(*getBasePackages(importingClassMetadata).toTypedArray())
        }
    }

}