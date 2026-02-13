package com.example.coffeemachine.data.repository

import com.example.coffeemachine.data.mapper.toBrew
import com.example.coffeemachine.data.mapper.toHeating
import com.example.coffeemachine.data.model.HeatingDto
import com.example.coffeemachine.data.repository.remote.CoffeeMachineRemote
import com.example.coffeemachine.domain.model.Brew
import com.example.coffeemachine.domain.model.Heating
import com.example.coffeemachine.domain.repository.CoffeeMachineRepo
import com.example.data.repository.remote.NetworkProvider

internal class CoffeeMachineRepoImpl(
    private val remoteCoffeeMachine: CoffeeMachineRemote
) : CoffeeMachineRepo {
    override suspend fun startHeating(): Heating {
        return remoteCoffeeMachine.sendHeating().toHeating()
    }

    override suspend fun startBrew(): Brew {
        return remoteCoffeeMachine.sendBrew().toBrew()
    }
}