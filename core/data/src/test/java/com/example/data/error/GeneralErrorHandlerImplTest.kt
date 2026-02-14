package com.example.data.error

import com.example.domain.model.SmartCoffeeMachineException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class GeneralErrorHandlerImplTest {


    @Test
    fun `when get error HttpException 500 it should map it to smart coffee machine exception for server` () {

        val response = Response.error<Any>(
            500,
            "error happened".toResponseBody(null)
        )
        val exception = HttpException(response)
        val errorHandler = GeneralErrorHandlerImpl()


        val result = errorHandler.getError(exception)

        assertTrue(result is SmartCoffeeMachineException.Server.InternalServerError)
    }

    @Test
    fun `when get error HttpException 400 it should map it to smart coffee machine exception for client` () {

        val response = Response.error<Any>(
            400,
            "error happened".toResponseBody(null)
        )
        val exception = HttpException(response)
        val errorHandler = GeneralErrorHandlerImpl()


        val result = errorHandler.getError(exception)

        assertTrue(result is SmartCoffeeMachineException.Client.Unhandled)
    }

    @Test
    fun `when get error IoException it should map it to smart coffee machine exception for UnKnown` () {

        val exception = IOException()
        val errorHandler = GeneralErrorHandlerImpl()


        val result = errorHandler.getError(exception)

        assertTrue(result is SmartCoffeeMachineException.Unknown)
    }


}