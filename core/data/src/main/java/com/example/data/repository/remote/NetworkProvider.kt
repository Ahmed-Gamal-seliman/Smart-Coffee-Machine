package com.example.data.repository.remote

import java.lang.reflect.Type

interface NetworkProvider {

    suspend fun <ResponseBody> get(
        responseWrappedModel: Type, pathUrl: String, headers: Map<String, Any>? = null,
        queryParams: Map<String, Any>? = null
    ): ResponseBody
}