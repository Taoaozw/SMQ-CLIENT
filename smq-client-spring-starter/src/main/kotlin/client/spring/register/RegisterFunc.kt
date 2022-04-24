package client.spring.register

import client.spring.annotation.*
import org.springframework.beans.factory.annotation.*
import org.springframework.beans.factory.support.*
import org.springframework.context.annotation.*
import org.springframework.core.env.*
import org.springframework.core.io.*
import org.springframework.core.type.*
import org.springframework.util.*

fun getScanner(
    environment: Environment,
    resource: ResourceLoader,
    registry: BeanDefinitionRegistry,
): ClassPathBeanDefinitionScanner {
    return object : ClassPathBeanDefinitionScanner(registry, false, environment, resource) {
        override fun isCandidateComponent(beanDefinition: AnnotatedBeanDefinition): Boolean {
            var isCandidate = false
            if (beanDefinition.metadata.isIndependent) {
                if (!beanDefinition.metadata.isAnnotation) {
                    isCandidate = true
                }
            }
            return isCandidate
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun getBasePackages(importingClassMetadata: AnnotationMetadata): Set<String> =
    buildSet {
        importingClassMetadata.getAnnotationAttributes(EnableMq::class.java.canonicalName)?.let { attr ->
            (attr[handlerValue] as Array<String>).filter { it.isNotBlank() }.forEach(this::add)
            (attr[handlerBasePackage] as Array<String>).filter { it.isNotBlank() }.forEach(this::add)
            (attr[handlerBasePackageClasses] as Array<String>).filter { it.isNotBlank() }
                .forEach(this::add)
        }
        if (isEmpty()) {
            add(ClassUtils.getPackageName(importingClassMetadata.className))
        }
    }


private const val handlerValue = "value"
private const val handlerBasePackage = "basePackages"
private const val handlerBasePackageClasses = "basePackageClasses"
