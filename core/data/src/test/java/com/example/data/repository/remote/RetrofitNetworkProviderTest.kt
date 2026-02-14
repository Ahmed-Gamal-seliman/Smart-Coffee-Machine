package com.example.data.repository.remote

import com.example.data.model.BaseDto
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkProviderTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var networkProvider: NetworkProvider

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/").toString()

        val apiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        networkProvider = RetrofitNetworkProvider(apiService)
    }

    @Test
    fun `when call get from retrofit network provider it should return base response and parse it`() =
        runTest {
            val baseResponse = """
            {
               "code":"200",
                "message": "successfully completed"
            }
        """.trimIndent()

            mockWebServer.enqueue(MockResponse().setBody(baseResponse))

            val response = networkProvider.get<BaseDto>(
                responseWrappedModel = BaseDto::class.java,
                pathUrl = "pathUrl",
                headers = null,
                queryParams = null

            )

            assertEquals("200", response.codeDto)
            assertEquals("successfully completed", response.messageDto)


        }


    @ParameterizedTest
    @ValueSource(ints = [400, 401, 404, 500, 502, 503])
    fun `when call get from retrofit network provider and server errors 400 or 500 and it should throw the Exception`(errorCode: Int) =
        runTest {
            val errorResponse = MockResponse()
                .setResponseCode(errorCode)
                .setBody(
                    """
                {"message": "Error occurred"}
                """.trimIndent()
                )

            mockWebServer.enqueue(errorResponse)


            val exception = assertThrows(HttpException::class.java) {
                runBlocking {
                    networkProvider.get<BaseDto>(
                        responseWrappedModel = BaseDto::class.java,
                        pathUrl = "testPath",
                        headers = null,
                        queryParams = null
                    )
                }
            }

            assertEquals(errorCode, exception.code())
        }


    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

}