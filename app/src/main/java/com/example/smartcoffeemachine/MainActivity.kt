package com.example.smartcoffeemachine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcoffeemachine.screens.coffee_machine.presentation.CoffeeMachineScreen
import com.example.smartcoffeemachine.screens.coffee_machine.presentation.CoffeeMachineViewModel
import com.example.smartcoffeemachine.screens.coffee_machine.presentation.MachineCoffeeApp
import com.example.smartcoffeemachine.ui.theme.SmartCoffeeMachineTheme
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCoffeeMachineTheme {
                MachineCoffeeApp()
            }
        }
    }
}

