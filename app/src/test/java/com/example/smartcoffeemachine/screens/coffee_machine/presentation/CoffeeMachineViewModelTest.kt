package com.example.smartcoffeemachine.screens.coffee_machine.presentation

import com.example.coffeemachine.domain.interactor.CoffeeMachineBrewUseCase
import com.example.coffeemachine.domain.interactor.CoffeeMachineHeatingUseCase
import com.example.coffeemachine.domain.model.Brew
import com.example.coffeemachine.domain.model.Heating
import com.example.domain.model.Resource
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoffeeMachineViewModelTest {

    private lateinit var viewModel: CoffeeMachineViewModel
    private val coffeeMachineBrewUsecase = mockk<CoffeeMachineBrewUseCase>()
    private val coffeeMachineHeatUsecase = mockk<CoffeeMachineHeatingUseCase>()


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CoffeeMachineViewModel(coffeeMachineHeatUsecase, coffeeMachineBrewUsecase,null)
    }

    //idle --> heat
    @Test
    fun `when i am in case idle state and click power on event the next state should be  heat state`() {
        viewModel.setState {
            copy(
                machineState = CoffeeMachineState.Idle
            )
        }

        every {
            coffeeMachineHeatUsecase.invoke(
                scope = any(),
                onResult = any()
            )
        } answers {
            val callback = secondArg<(Resource<Heating>) -> Unit>()
            callback(Resource.Success(Heating(state = "heating completed")))
        }

        viewModel.setEvent(
            CoffeeMachineContract.Event.MachineCoffeeEvent(
                CoffeeMachineEvent.PowerOnClicked
            )
        )

        assertEquals(CoffeeMachineState.Heat, viewModel.viewState.value.machineState)

    }

    //heat --> ready
    @Test
    fun `when i am in case heat state and the heating is success the next state should be  ready state`() {

        viewModel.setState {
            copy(
                machineState = CoffeeMachineState.Heat
            )
        }


        viewModel.setEvent(
            CoffeeMachineContract.Event.MachineCoffeeEvent(
                CoffeeMachineEvent.HeatingSuccess
            )
        )

        assertEquals(CoffeeMachineState.Ready, viewModel.viewState.value.machineState)

    }

    //ready -->  brew
    @Test
    fun `when i am in case ready state and the brew event started the next state should be  brew state`() {

        viewModel.setState {
            copy(
                machineState = CoffeeMachineState.Ready
            )
        }

        every {
            coffeeMachineBrewUsecase.invoke(
                scope = any(),
                onResult = any()
            )
        } answers {
            val callback = secondArg<(Resource<Brew>) -> Unit>()
            callback(Resource.Success(Brew(state = "brewing completed")))
        }


        viewModel.setEvent(
            CoffeeMachineContract.Event.MachineCoffeeEvent(
                CoffeeMachineEvent.StartBrewClicked
            )
        )

        assertEquals(CoffeeMachineState.Brew, viewModel.viewState.value.machineState)

    }

    //brew --> reset it to initial state Idle
    @Test
    fun `when i am in case brew state and the brewing is successfully completed it should reset to the initial state Idle state`() {

        viewModel.setState {
            copy(
                machineState = CoffeeMachineState.Brew
            )
        }


        viewModel.setEvent(
            CoffeeMachineContract.Event.MachineCoffeeEvent(
                CoffeeMachineEvent.BrewingSuccess
            )
        )

        assertEquals(CoffeeMachineState.Idle, viewModel.viewState.value.machineState)

    }


    //failure --> reset it to initial state Idle
    @Test
    fun `when i am in  failure state the next state should reset to  Idle state`() {

        viewModel.setState {
            copy(
                machineState = CoffeeMachineState.Error(error="error happened")
            )
        }


        viewModel.setEvent(
            CoffeeMachineContract.Event.MachineCoffeeEvent(
                CoffeeMachineEvent.Reset
            )
        )


        assertEquals(CoffeeMachineState.Idle, viewModel.viewState.value.machineState)

    }


    //brew cancel --> reset it to initial state Idle
    @Test
    fun `when i am in  brew  state and cancel brew event happen the next state should reset to  Idle state`() {

        viewModel.setState {
            copy(
                machineState = CoffeeMachineState.Brew
            )
        }

        every { coffeeMachineBrewUsecase.cancelJob() } just Runs



        viewModel.setEvent(
            CoffeeMachineContract.Event.MachineCoffeeEvent(
                CoffeeMachineEvent.BrewingCanceled
            )
        )

        assertEquals(CoffeeMachineState.Idle, viewModel.viewState.value.machineState)

    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

}

