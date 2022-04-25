package client.spring.support

import client.spring.exception.*
import org.springframework.expression.*
import org.springframework.expression.spel.standard.*
import org.springframework.expression.spel.support.*
import org.springframework.http.server.*
import org.springframework.web.util.pattern.*
import java.lang.reflect.*
import java.util.concurrent.*


class RequestMappingCache {

    private val parser: PathPatternParser = PathPatternParser()

    private val pathLookup: MutableMap<String, MethodHandler> = ConcurrentHashMap(8)

    private val requestCache: MutableMap<String, MethodHandler> = ConcurrentHashMap(8)


    fun register(mapping: PathMappingInfo, handler: Class<*>, method: Method) {
        pathLookup[mapping.name] = MethodHandler(mapping.name, method, handler)
    }

    operator fun get(request: String): MethodHandler {
        if (requestCache.contains(request)) {
            return requestCache[request]!!
        } else {
            for (path in pathLookup) {
                if (parser.parse(path.key).matches(PathContainer.parsePath(request))) {
                    requestCache[request] = path.value
                    return path.value
                }
            }
            throw NotFoundSubscriberPathException("Not found subscriber path: $request")
        }
    }

    fun getPathVariable(request: String, path: String): MutableMap<String, String>? =
        parser.parse(path).matchAndExtract(PathContainer.parsePath(request))?.uriVariables

}

fun main() {
    val parser: ExpressionParser = SpelExpressionParser()
    val exp: Expression = parser.parseExpression("#primes")
    val context = StandardEvaluationContext()
    context.setVariable("primes", 1)

    val message = exp.getValue(context, String::class.java)
    print(message)
}