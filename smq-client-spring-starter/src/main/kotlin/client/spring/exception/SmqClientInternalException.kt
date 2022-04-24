package client.spring.exception

class NotFoundSubscriberPathException(msg: String) :RuntimeException(msg)

class NotFoundPathVariableException(msg: String) : RuntimeException(msg)

class UnSupportedPathVariableException(msg: String) : RuntimeException(msg)

class NotFoundMqttProtocolException(msg: String) : RuntimeException(msg)

class UnsupportedPathParameterTypeException(msg: String) : RuntimeException(msg)

class NoFoundPathInMqEndpointException(msg: String) : RuntimeException(msg)

class PublishFunctionParameterNullException(msg: String) : RuntimeException(msg)