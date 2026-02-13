package com.example.coffeemachine.domain.interactor

import com.example.coffeemachine.domain.model.Brew
import com.example.coffeemachine.domain.repository.CoffeeMachineRepo
import com.example.domain.interactor.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoffeeMachineBrewUseCase(
    private val coffeeMachineRepo: CoffeeMachineRepo
): BaseUseCase<Brew>() {

    override fun executeDS(): Flow<Brew> {
        return flow{
            val response= coffeeMachineRepo.startBrew()
            emit(response)
        }
    }
}