package com.example.smartcoffeemachine.screens.coffee_machine.presentation

import com.example.smartcoffeemachine.mvi.UIEvent
import com.example.smartcoffeemachine.mvi.UIState

class CoffeeMachineContract {
    sealed interface Event: UIEvent{

        data class MachineCoffeeEvent(val machineEvent: CoffeeMachineEvent):Event
        data class HasCancelButton(val hasCancelButton:Boolean): Event
        data class HasErrorChanged(val hasError: Boolean): Event
        data class IsMachineCoffeeCompletedChanged(val isMachineCoffeeCompleted: Boolean): Event


    }

    data class State(
        override val isLoading: Boolean=false,
        val hasError:Boolean=false,
        val errorMessage:String?=null,
        val hasCancelButton: Boolean=false,
        val isMachineCoffeeCompleted:Boolean=false,
        var machineState: CoffeeMachineState= CoffeeMachineState.Idle,
    ): UIState
}

sealed interface CoffeeMachineEvent{
    data object PowerOnClicked: CoffeeMachineEvent
    data object StartBrewClicked: CoffeeMachineEvent
    data object HeatingSuccess : CoffeeMachineEvent
    data object BrewingSuccess : CoffeeMachineEvent

    data object BrewingCanceled:CoffeeMachineEvent


    data class Failure(val message: String?) : CoffeeMachineEvent
    data object Reset: CoffeeMachineEvent

}
sealed interface CoffeeMachineState{
    data object Idle: CoffeeMachineState
    data object  Heat: CoffeeMachineState
    data object  Ready: CoffeeMachineState

    data object Brew: CoffeeMachineState

    data class Error(val error:String?): CoffeeMachineState
}


