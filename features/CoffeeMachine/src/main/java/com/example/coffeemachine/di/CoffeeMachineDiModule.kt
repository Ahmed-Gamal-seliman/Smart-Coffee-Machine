package com.example.coffeemachine.di

import com.example.coffeemachine.data.repository.CoffeeMachineRepoImpl
import com.example.coffeemachine.data.repository.remote.CoffeeMachineRemote
import com.example.coffeemachine.data.repository.remote.CoffeeMachineRemoteImpl
import com.example.coffeemachine.domain.interactor.CoffeeMachineBrewUseCase
import com.example.coffeemachine.domain.interactor.CoffeeMachineHeatingUseCase
import com.example.coffeemachine.domain.repository.CoffeeMachineRepo
import org.koin.dsl.module

val coffeeMachineDiModule = module {
    single { CoffeeMachineBrewUseCase(get()) }
    single { CoffeeMachineHeatingUseCase(get()) }
    single<CoffeeMachineRepo> { CoffeeMachineRepoImpl(get()) }


    single<CoffeeMachineRemote> { CoffeeMachineRemoteImpl(get()) }
}