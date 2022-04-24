package client.spring.extension

import client.spring.annotation.*
import mu.*
import kotlin.reflect.*
import kotlin.reflect.full.*

val log = KotlinLogging.logger {}


fun KType.notUnit() = this != Unit::class.createType()


fun KClass<*>.isEndPoint() = hasAnnotation<MqEndpoint>()

fun KFunction<*>.isPublisher() = hasAnnotation<MqPath>()

fun KFunction<*>.isToStringFunction() = toStringFunctionName == name

fun KFunction<*>.isEqualsFunction() = equalsFunctionName == name

fun KFunction<*>.isHashCodeFunction() = hashCodeFunctionName == name

fun KFunction<*>.checkDecoder() = valueParameters.size == 1 && returnType.notUnit()

fun KFunction<*>.checkEncoder() = valueParameters.size == 1


fun String.isVariable() = this.startsWith("{") && this.endsWith("}")


const val toStringFunctionName = "toString"

const val hashCodeFunctionName = "hashCode"

const val equalsFunctionName = "equals"