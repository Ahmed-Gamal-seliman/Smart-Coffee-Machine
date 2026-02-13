package com.example.domain.model

sealed class Resource<out Model> {
        data class Loading(val isLoading: Boolean) : Resource<Nothing>()

        data class Success<out Model>(val model: Model) : Resource<Model>()

        data class Failure(val exception: SmartCoffeeMachineException) : Resource<Nothing>()
}