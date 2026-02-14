package com.example.smartcoffeemachine.screens.coffee_machine.presentation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MachineCoffeeApp(){
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.systemBarsPadding()) {
        NavHost(
            navController = navController,
            startDestination = CoffeeMachineDestination
        ) {
            composable<CoffeeMachineDestination> {
                CoffeeMachineScreen(
                    coffeeMachineViewModel = koinViewModel<CoffeeMachineViewModel>()
                )
            }

        }

    }
}
@Serializable
object CoffeeMachineDestination