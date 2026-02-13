package com.example.domain.error

import com.example.domain.model.SmartCoffeeMachineException

interface ErrorHandler {

    fun getError(throwable: Throwable): SmartCoffeeMachineException
}