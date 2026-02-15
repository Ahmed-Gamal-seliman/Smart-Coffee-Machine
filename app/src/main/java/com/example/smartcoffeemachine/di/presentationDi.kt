package com.example.smartcoffeemachine.di

import androidx.lifecycle.SavedStateHandle
import com.example.smartcoffeemachine.screens.coffee_machine.presentation.CoffeeMachineViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationDiModule= module{
    viewModel {CoffeeMachineViewModel(get(),get(),get()) }
}