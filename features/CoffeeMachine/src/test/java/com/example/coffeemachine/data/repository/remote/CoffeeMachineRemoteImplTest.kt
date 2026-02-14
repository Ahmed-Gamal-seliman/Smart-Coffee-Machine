package com.example.coffeemachine.data.repository.remote

import android.net.http.HttpException
import com.example.coffeemachine.data.model.BrewDto
import com.example.coffeemachine.data.model.HeatingDto
import com.example.coffeemachine.domain.model.Brew
import com.example.data.repository.remote.NetworkProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoffeeMachineRemoteImplTest {

    private lateinit var coffeeMachineRemote: CoffeeMachineRemote
    private val networkProvider = mockk<NetworkProvider>()

    @BeforeEach
    fun setUp() {
        coffeeMachineRemote= CoffeeMachineRemoteImpl(networkProvider)
    }

    @Test
    fun `when call sendHeating request from remote datasource it must get data from api`()= runTest{

        val expectedDto = HeatingDto(state= "heating").apply {
            codeDto= "200"
            messageDto = "heating successfully completed"
        }

        coEvery {  networkProvider.get<HeatingDto>(
            responseWrappedModel = HeatingDto::class.java,
            pathUrl = "startHeat",
            null,
            null
        ) } returns expectedDto

        val response = coffeeMachineRemote.sendHeating()


        assertEquals(expectedDto.codeDto, response.codeDto)
        assertEquals(expectedDto.state, response.state)
        assertEquals(expectedDto.messageDto, response.messageDto)

        coVerify(exactly = 1) {
           networkProvider.get(
               responseWrappedModel = HeatingDto::class.java,
               pathUrl = "startHeat",
               headers = null,
               queryParams = null
           )
       }

    }

    @Test
    fun `when call sendHeating request from remote datasource and the server respond with exception it must throw the exception as it is`()= runTest{


        coEvery {  networkProvider.get<HeatingDto>(
            responseWrappedModel = HeatingDto::class.java,
            pathUrl = "startHeat",
            null,
            null
        ) } throws HttpException("error", null)




        assertThrows(HttpException::class.java) {
            runBlocking {
                coffeeMachineRemote.sendHeating()
            }
        }

        coVerify(exactly = 1) {
            networkProvider.get(
                responseWrappedModel = HeatingDto::class.java,
                pathUrl = "startHeat",
                headers = null,
                queryParams = null
            )
        }

    }



    @Test
    fun `when call sendBrew request from remote datasource it must get data from api`()= runTest{

        val expectedDto = BrewDto(state= "brew").apply {
            codeDto= "200"
            messageDto = "brewing successfully completed"
        }

        coEvery {  networkProvider.get<BrewDto>(
            responseWrappedModel = BrewDto::class.java,
            pathUrl = "startBrew",
            null,
            null
        ) } returns expectedDto

        val response = coffeeMachineRemote.sendBrew()


        assertEquals(expectedDto.codeDto, response.codeDto)
        assertEquals(expectedDto.state, response.state)
        assertEquals(expectedDto.messageDto, response.messageDto)

        coVerify(exactly = 1) {
            networkProvider.get(
                responseWrappedModel = BrewDto::class.java,
                pathUrl = "startBrew",
                headers = null,
                queryParams = null
            )
        }

    }

    @Test
    fun `when call sendBrew request from remote datasource and the server respond with exception it must throw the exception as it is`()= runTest{


        coEvery {  networkProvider.get<BrewDto>(
            responseWrappedModel = BrewDto::class.java,
            pathUrl = "startBrew",
            null,
            null
        ) } throws HttpException("error", null)




        assertThrows(HttpException::class.java) {
            runBlocking {
                coffeeMachineRemote.sendBrew()
            }
        }

        coVerify(exactly = 1) {
            networkProvider.get(
                responseWrappedModel = BrewDto::class.java,
                pathUrl = "startBrew",
                headers = null,
                queryParams = null
            )
        }

    }





}