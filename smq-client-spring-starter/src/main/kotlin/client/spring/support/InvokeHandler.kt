package client.spring.support

import org.springframework.context.*
import org.springframework.context.event.*
import smq.client.core.*


class InvokeHandler(
    private val context: ApplicationContext,
    private val register: RequestMappingCache,
    private val codec: MqCodecMappingHandler,
) {

    @EventListener(value = [SmqPublishMessage::class])
    fun request(msg: SmqPublishMessage) = register[msg.topic].apply {
        invoke(context, args(msg, register.getPathVariable(msg.topic, this.path), codec.decoderProvider))
    }


}