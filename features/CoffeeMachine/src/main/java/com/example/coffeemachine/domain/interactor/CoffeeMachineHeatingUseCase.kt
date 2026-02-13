package com.example.coffeemachine.domain.interactor

import com.example.coffeemachine.domain.model.Heating
import com.example.coffeemachine.domain.repository.CoffeeMachineRepo
import com.example.domain.interactor.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoffeeMachineHeatingUseCase(
    private val coffeeMachineRepo: CoffeeMachineRepo
): BaseUseCase<Heating>() {
    override fun executeDS(): Flow<Heating> {
       return flow {
            val response = coffeeMachineRepo.startHeating()
           emit(response)
        }
    }
}