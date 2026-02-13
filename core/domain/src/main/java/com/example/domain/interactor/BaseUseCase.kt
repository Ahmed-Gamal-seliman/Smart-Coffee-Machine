package com.example.domain.interactor

import com.example.domain.error.ErrorHandler
import com.example.domain.model.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseUseCase<out Domain>: KoinComponent {

    protected val errorHandler: ErrorHandler by inject()
    protected var job: Job?=null


    protected abstract fun executeDS(): Flow<Domain>

    operator fun invoke(
        scope: CoroutineScope,
        onResult: (Resource<Domain>) -> Unit
    ){
        job = scope.launch(Dispatchers.Main) {
            onResult(Resource.Loading(isLoading = true))
            runFlow(executeDS(), onResult).collect {
                onResult.invoke(
                    Resource.Success(it)
                )
                onResult(Resource.Loading(isLoading = false))
            }
        }
    }

    fun cancelJob(){
        job?.cancel()
    }

    private fun <M> runFlow(
        requestExecution: Flow<M>, onResult: suspend (Resource<Domain>) -> Unit
    ): Flow<M> = requestExecution.catch { e ->
        onResult(Resource.Failure(errorHandler.getError(e)))
        onResult.invoke(Resource.Loading(false))
    }.flowOn(Dispatchers.IO)
}