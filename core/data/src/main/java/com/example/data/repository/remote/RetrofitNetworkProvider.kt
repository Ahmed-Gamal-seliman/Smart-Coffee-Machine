package com.example.data.repository.remote

import com.example.data.utils.getModelFromJSON
import java.lang.reflect.Type

class RetrofitNetworkProvider(
    private val apiService: ApiService
): NetworkProvider {

    override suspend fun <ResponseBody> get(
        responseWrappedModel: Type,
        pathUrl: String,
        headers: Map<String, Any>?,
        queryParams: Map<String, Any>?
    ): ResponseBody {
        val response = apiService.get(
            pathUrl=pathUrl,
            headerMap = headers ?: hashMapOf(),
            queryParams = queryParams ?: hashMapOf()
        )

        return response.string().getModelFromJSON(responseWrappedModel)
    }
}