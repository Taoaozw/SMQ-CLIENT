package client.spring.extension

import client.spring.exception.*


object Convertor {
    fun <T> convertPath(source: String, targetClazz: Class<T>): Any {
        return when (targetClazz) {
            String::class.java -> source
            Int::class.java -> source.toInt()
            Long::class.java -> source.toLong()
            Double::class.java -> source.toDouble()
            Float::class.java -> source.toFloat()
            Boolean::class.java -> source.toBoolean()
            else -> throw UnsupportedPathParameterTypeException("Unsupported  path parameter type: ${targetClazz.name}")
        }
    }

    fun  convertToString(source: Any): String {
        return when (source) {
            is String -> source
            is Int -> source.toString()
            is Long -> source.toString()
            is Double -> source.toString()
            is Float -> source.toString()
            is Boolean -> source.toString()
            else -> throw UnsupportedPathParameterTypeException("Unsupported  path parameter type: ${source.javaClass.name}")
        }
    }
}

