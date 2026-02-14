package com.example.smartcoffeemachine.screens.coffee_machine.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartcoffeemachine.mvi.UISideEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun CoffeeMachineScreen(
    coffeeMachineViewModel: CoffeeMachineViewModel
) {
    val state by coffeeMachineViewModel.viewState.collectAsStateWithLifecycle()
    CoffeeMachineContent(
        state = state,
        onEvent = coffeeMachineViewModel::setEvent,
        effect = coffeeMachineViewModel.effect
    )
}

@Composable
fun CoffeeMachineContent(
    state: CoffeeMachineContract.State,
    onEvent: (CoffeeMachineContract.Event) -> Unit,
    effect: Flow<UISideEffect>? = null
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { Text("Smart Coffee Machine") }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()

            ) {
                Button(
                    onClick = {
                        if (state.machineState == CoffeeMachineState.Idle) {
                            onEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.PowerOnClicked
                                )
                            )
                        } else if (state.machineState == CoffeeMachineState.Ready) {
                            onEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.StartBrewClicked
                                )
                            )
                            onEvent(CoffeeMachineContract.Event.HasCancelButton(true))
                        }

                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(state.machineState.toString())

                }





                if (state.hasCancelButton) {
                    Button(
                        onClick = {
                            onEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.BrewingCanceled
                                )
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Red)
                    ) {

                        Text("Cancel")
                    }
                }
            }




            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (state.hasError) {

                state.errorMessage?.let {
                    ConfirmDialog(
                        show = true,
                        message = it,
                        onConfirm = {
                            onEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.Reset
                                )
                            )
                            onEvent(CoffeeMachineContract.Event.HasErrorChanged(false))
                        },
                        onDismiss = {
                            onEvent(
                                CoffeeMachineContract.Event.MachineCoffeeEvent(
                                    CoffeeMachineEvent.Reset
                                )
                            )
                            onEvent(CoffeeMachineContract.Event.HasErrorChanged(false))
                        }
                    )
                }

            }

            if (state.isMachineCoffeeCompleted) {
                ConfirmDialog(
                    show = true,
                    message = "coffee is completed successfully",
                    onConfirm = {
                        onEvent(CoffeeMachineContract.Event.IsMachineCoffeeCompletedChanged(false))
                    },
                    onDismiss = {
                        onEvent(CoffeeMachineContract.Event.IsMachineCoffeeCompletedChanged(false))
                    }
                )
            }


        }

    }

}

@Composable
fun ConfirmDialog(
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    message: String
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,

            text = {
                Text(message)
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}



