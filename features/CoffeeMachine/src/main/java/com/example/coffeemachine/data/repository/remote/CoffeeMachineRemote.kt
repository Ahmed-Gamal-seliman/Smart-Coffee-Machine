package com.example.coffeemachine.data.repository.remote

import com.example.coffeemachine.data.model.BrewDto
import com.example.coffeemachine.data.model.HeatingDto

internal interface CoffeeMachineRemote {

    suspend fun sendHeating(): HeatingDto
    suspend fun sendBrew(): BrewDto
}