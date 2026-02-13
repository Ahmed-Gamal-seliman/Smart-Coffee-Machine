package com.example.smartcoffeemachine.mvi

interface UIEvent

interface UIState {
    val isLoading: Boolean
}

interface UISideEffect