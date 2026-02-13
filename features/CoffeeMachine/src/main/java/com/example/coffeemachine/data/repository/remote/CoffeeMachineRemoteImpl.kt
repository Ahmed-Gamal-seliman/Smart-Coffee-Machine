package com.example.coffeemachine.data.repository.remote

import com.example.coffeemachine.data.model.BrewDto
import com.example.coffeemachine.data.model.HeatingDto
import com.example.data.repository.remote.NetworkProvider

internal class CoffeeMachineRemoteImpl(
    private val networkProvider: NetworkProvider
): CoffeeMachineRemote {

    override suspend fun sendHeating(): HeatingDto {
        return networkProvider.get(
            responseWrappedModel = HeatingDto::class.java,
            pathUrl = "startHeat",
            headers = null,
            queryParams = null
        )
    }

    override suspend fun sendBrew(): BrewDto {
        return networkProvider.get(
            responseWrappedModel = BrewDto::class.java,
            pathUrl = "startBrew",
            headers = null,
            queryParams = null
        )
    }
}