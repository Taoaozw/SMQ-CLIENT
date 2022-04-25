package client.spring

import client.spring.receiver.*
import client.spring.support.*
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.*


@Configuration
class SmqConfiguration {


    @Bean
    fun mappingCache() = RequestMappingCache()

    @Bean
    fun mqCodecMappingHandler(context: ApplicationContext) = MqCodecMappingHandler(context)

    @Bean
    fun mqRequestMappingHandler(mappingCache: RequestMappingCache, codec: MqCodecMappingHandler) =
        MqttRequestMappingHandler(codec, mappingCache)

    @Bean
    fun invokerHandler(mappingCache: RequestMappingCache, context: ApplicationContext, codec: MqCodecMappingHandler) =
        InvokeHandler(context, mappingCache, codec)

}