package com.example.data.error

import com.example.domain.error.ErrorHandler
import com.example.domain.model.SmartCoffeeMachineException
import retrofit2.HttpException


internal class GeneralErrorHandlerImpl: ErrorHandler {
    override fun getError(throwable: Throwable): SmartCoffeeMachineException {
        return when (throwable) {
            is HttpException -> mapHttpStatusCode(throwable)

            else -> SmartCoffeeMachineException.Unknown(throwable.message)
        }
    }
}

private fun mapHttpStatusCode(exception: HttpException): SmartCoffeeMachineException {


    val message = exception.message
    val code = exception.code()


    return when (exception.code()) {

        401 -> SmartCoffeeMachineException.Client.Unauthorized(message)


        in 400..499 -> SmartCoffeeMachineException.Client.Unhandled(code, message)


        in 500..599 -> SmartCoffeeMachineException.Server.InternalServerError(code, message)


        else -> SmartCoffeeMachineException.Unknown(message)
    }
}
