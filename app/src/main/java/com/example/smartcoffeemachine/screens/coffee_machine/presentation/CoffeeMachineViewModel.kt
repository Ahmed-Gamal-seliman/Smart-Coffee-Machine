package com.example.smartcoffeemachine.screens.coffee_machine.presentation

import androidx.lifecycle.viewModelScope
import com.example.coffeemachine.domain.interactor.CoffeeMachineBrewUseCase
import com.example.coffeemachine.domain.interactor.CoffeeMachineHeatingUseCase
import com.example.domain.model.BaseDomain
import com.example.domain.model.Resource
import com.example.smartcoffeemachine.mvi.BaseViewModel
import com.example.smartcoffeemachine.screens.coffee_machine.presentation.CoffeeMachineState.*

class CoffeeMachineViewModel(
    private val coffeeMachineHeatingUseCase: CoffeeMachineHeatingUseCase,
    private val coffeeMachineBrewUseCase: CoffeeMachineBrewUseCase
) : BaseViewModel<CoffeeMachineContract.Event, CoffeeMachineContract.State>() {
    override fun setInitialState(): CoffeeMachineContract.State = CoffeeMachineContract.State()


    override fun handleEvent(event: CoffeeMachineContract.Event) {


        when (event) {

            is CoffeeMachineContract.Event.MachineCoffeeEvent -> {
                handleCoffeeMachineState(event.machineEvent)
            }

            is CoffeeMachineContract.Event.HasErrorChanged -> onHasErrorChanged(event.hasError)
            is CoffeeMachineContract.Event.HasCancelButton -> onHasCancelButtonChanged(event.hasCancelButton)
            is CoffeeMachineContract.Event.IsMachineCoffeeCompletedChanged -> onIsMachineCoffeeCompletedChanged(event.isMachineCoffeeCompleted)
        }

    }

    private fun onIsMachineCoffeeCompletedChanged(machineCoffeeCompleted: Boolean) {
        setState { copy(
            isMachineCoffeeCompleted = machineCoffeeCompleted
        ) }
    }

    fun handleCoffeeMachineState(machineEvent: CoffeeMachineEvent) {
        val current = viewState.value.machineState

        val newState = when (current) {

            is Idle -> {
                when (machineEvent) {
                    CoffeeMachineEvent.PowerOnClicked -> {
                        startHeating()
                        Heat

                    }

                    else -> current
                }
            }

            is Heat -> {
                when (machineEvent) {
                    CoffeeMachineEvent.HeatingSuccess -> Ready
                    is CoffeeMachineEvent.Failure -> {
                        setState {
                            copy(
                                hasError = true,
                                errorMessage = machineEvent.message
                            )
                        }
                        Error(machineEvent.message)

                    }

                    else -> current
                }
            }

            is Ready -> {
                when (machineEvent) {
                    CoffeeMachineEvent.StartBrewClicked -> {
                        startBrew()
                        Brew
                    }

                    else -> current
                }
            }

            is Brew -> {
                when (machineEvent) {
                    CoffeeMachineEvent.BrewingSuccess -> {
                        setState { copy(hasCancelButton = false,
                                isMachineCoffeeCompleted = true
                            ) }
                        Idle
                    }

                    CoffeeMachineEvent.BrewingCanceled -> {
                        coffeeMachineBrewUseCase.cancelJob()
                        setState {
                            copy(
                                isLoading = false,
                                hasCancelButton = false
                            )
                        }
                        Idle
                    }

                    is CoffeeMachineEvent.Failure -> {
                        setState {
                            copy(
                                hasError = true,
                                errorMessage = machineEvent.message,
                                hasCancelButton = false
                            )
                        }
                        Error(machineEvent.message)

                    }

                    else -> current
                }
            }

            is Error -> {
                when (machineEvent) {
                    CoffeeMachineEvent.Reset -> Idle
                    else -> current
                }
            }
        }
        setState {
            copy(machineState = newState)
        }

    }

    private fun onHasCancelButtonChanged(hasCancelButton: Boolean) {
        setState {
            copy(
                hasCancelButton = hasCancelButton
            )
        }
    }


    private fun onHasErrorChanged(hasError: Boolean) {
        setState {
            copy(
                hasError = hasError
            )
        }
    }

    private fun startBrew() {
        coffeeMachineBrewUseCase.invoke(
            viewModelScope
        ) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    setState { copy(isLoading = resource.isLoading) }
                }

                is Resource.Success -> {
                    handleResponse(
                        resource.model,
                        onSuccess = {
                            handleEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.BrewingSuccess
                                )
                            )
                        },
                        onFailure = {
                            handleEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.Failure(it)
                                )
                            )
                        })
                }


                is Resource.Failure -> handleEvent(
                    CoffeeMachineContract.Event.MachineCoffeeEvent(CoffeeMachineEvent.Failure((resource.exception.message)))
                )


            }
        }
    }

    private fun handleResponse(
        response: BaseDomain,
        onSuccess: () -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        when (response.code) {
            "200" -> onSuccess()


            else -> onFailure(response.message)

        }

    }

    private fun startHeating() {
        coffeeMachineHeatingUseCase.invoke(
            viewModelScope
        ) { resource ->
            when (resource) {
                is Resource.Loading -> setState { copy(isLoading = resource.isLoading) }

                is Resource.Success ->
                    handleResponse(
                        resource.model,
                        onSuccess = {
                            handleEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(CoffeeMachineEvent.HeatingSuccess)
                            )
                        },
                        onFailure = {
                            handleEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.Failure(it)
                                )
                            )
                        })




                is Resource.Failure -> handleEvent(
                    CoffeeMachineContract.Event.MachineCoffeeEvent(
                        CoffeeMachineEvent.Failure(
                            resource.exception.message
                        )
                    )
                )


            }

        }
    }


}