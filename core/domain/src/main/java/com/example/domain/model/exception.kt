package com.example.domain.model

sealed class SmartCoffeeMachineException(message: String?) : Exception(message) {

    sealed class Client(message: String? = null) : SmartCoffeeMachineException(message) {
        data class Unauthorized(override val message:String?) : Client(message =message ?: "Unauthorized client error...")

        data class Unhandled(val httpErrorCode: Int, override val message: String?) : Client(
            message = "Unhandled client error with code:${httpErrorCode}, and the failure reason: $message"
        )
    }

    sealed class Server(message: String? = null) : SmartCoffeeMachineException(message) {
        data class InternalServerError(val httpErrorCode: Int, override val message: String?) :
            Server(message = "Internal server error with code:${httpErrorCode}, and the failure reason: $message")
    }


    data class Unknown(override val message: String?) : SmartCoffeeMachineException(message ?: "unKnown error..")

}