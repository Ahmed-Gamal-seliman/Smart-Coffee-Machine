package com.example.coffeemachine.domain.repository

import com.example.coffeemachine.domain.model.Brew
import com.example.coffeemachine.domain.model.Heating

interface CoffeeMachineRepo {

    suspend fun startHeating(): Heating
    suspend fun startBrew(): Brew
}