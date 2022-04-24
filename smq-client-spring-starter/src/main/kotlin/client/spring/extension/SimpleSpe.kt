package client.spring.extension

import client.spring.exception.*
import org.springframework.util.*

object SimpleSpe {

    /**
     * store the parse result of the same parameter
     */
    private val map: MutableMap<Pair<String, VariableContext>, String> = ConcurrentReferenceHashMap(8)

    //fixme /abc/a 这种需要考虑进去
    fun parse(expression: String, context: VariableContext) =
        map[expression to context] ?: expression.split("/").mapNotNull(::convert)
            .joinToString(separator = "/") { it.expand(context) }.also {
                map[expression to context] = it
            }


    /**
     * Expression matching is performed for each segment of the subscription path.
     */
    private fun convert(spilt: String): SimpleExpression? = when {
        spilt.isVariable() -> VariableExpression(spilt, spilt.substring(1, spilt.length - 1))
        spilt.isBlank() -> {
            log.warn { "Middle of two separate '/' is blank , merge the head and tail!" }
            null
        }
        else -> {
            CommonExpression(spilt)
        }
    }

}


@JvmInline
value class VariableContext(private val variables: MutableMap<String, Any> = mutableMapOf()) {
    operator fun get(key: String) = variables[key]

    operator fun set(key: String, value: Any) {
        variables[key] = value
    }
}

sealed interface SimpleExpression {

    val value: String

    fun expand(context: VariableContext): String
}

class CommonExpression(override val value: String) : SimpleExpression {
    override fun expand(context: VariableContext): String = value
}

class VariableExpression(override val value: String, private val name: String) : SimpleExpression {
    override fun expand(context: VariableContext): String =
        (context[name]?.let { Convertor.convertToString(it) }
            ?: throw NotFoundPathVariableException("Not found Path variable <$name>"))
}

