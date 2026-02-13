package com.example.data.di

import com.example.data.error.GeneralErrorHandlerImpl
import com.example.domain.error.ErrorHandler
import org.koin.dsl.module

 val errorHandlerModule = module{
    single<ErrorHandler> { GeneralErrorHandlerImpl()  }
}