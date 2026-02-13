package com.example.data.di

import com.example.data.BuildConfig
import com.example.data.repository.remote.ApiService
import com.example.data.repository.remote.NetworkProvider
import com.example.data.repository.remote.RetrofitNetworkProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<Gson> {
        GsonBuilder().create()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }

    single<NetworkProvider> {
        RetrofitNetworkProvider(get())
    }


    single<OkHttpClient.Builder> {
        OkHttpClient().newBuilder().apply {
            addInterceptor(
                getHttpLoggingInterceptor()
            )
        }
    }

    single<Retrofit> {
        Retrofit
            .Builder()
            .client(get<OkHttpClient.Builder>().build())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}

private fun getHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
    else HttpLoggingInterceptor.Level.NONE
}