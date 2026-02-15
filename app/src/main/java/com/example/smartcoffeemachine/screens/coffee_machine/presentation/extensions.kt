package com.example.smartcoffeemachine.screens.coffee_machine.presentation

fun String.toCoffeeState(): CoffeeMachineState {
    return when {
        this == "Idle" -> CoffeeMachineState.Idle
        this == "Heat" -> CoffeeMachineState.Heat
        this == "Ready" -> CoffeeMachineState.Ready
        this == "Brew" -> CoffeeMachineState.Brew
        this == "Error" -> CoffeeMachineState.Error(this)

        else -> CoffeeMachineState.Idle
    }
}