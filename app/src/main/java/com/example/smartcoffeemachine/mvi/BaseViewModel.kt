package com.example.smartcoffeemachine.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UIEvent, State : UIState> : ViewModel() {
    private val initialState: State by lazy { setInitialState() }

    // StateFlow
    private val _viewState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val viewState: StateFlow<State> = _viewState

    // Event
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    //Effect
    private val _effect: Channel<UISideEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    abstract fun setInitialState(): State




    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvent(it)
            }
        }
    }

    protected abstract fun handleEvent(event: Event)

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }


    fun setState(reducer: State.() -> State) {
        _viewState.update(reducer)
    }

    fun setEffect(builder: () -> UISideEffect) {
        val effectValue = builder()
        _effect.trySend(effectValue)
    }

}
