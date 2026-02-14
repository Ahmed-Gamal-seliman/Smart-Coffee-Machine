package com.example.smartcoffeemachine

import android.app.Application
import com.example.coffeemachine.di.coffeeMachineDiModule
import com.example.data.di.errorHandlerModule
import com.example.data.di.networkModule
import com.example.smartcoffeemachine.di.presentationDiModule
import org.koin.core.context.startKoin

class CoffeeMachineApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                coffeeMachineDiModule,
                errorHandlerModule,
                networkModule,
                presentationDiModule


            )
        }
    }
}