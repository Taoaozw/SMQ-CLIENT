package smq.client.core

interface SmqPublishMessage {

    val payload:Any

    val topic:String
}