package fr.gilles.riceattend.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.Page
import fr.gilles.riceattend.services.entities.models.Params
import fr.gilles.riceattend.services.entities.models.Worker
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkersVM @Inject constructor(
    private val apiEndpoint: ApiEndpoint
)  : ViewModel() {
    var searchState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { true },
            errorMessage = { "" },
        )
    )
    var params by mutableStateOf(Params())
    var workers by mutableStateOf<Page<Worker>?>(null)
    var loading by mutableStateOf(false)
    var workerFormVM by mutableStateOf(WorkerFormVM())
    var creationLoading by mutableStateOf(false)


    init {
        loadWorkers()
    }

    private fun loadWorkers() {
        loading = true

        viewModelScope.launch {
            Log.d("WorkersViewModel", "loadWorkers")
            apiEndpoint.workerRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<Worker>>() {
                    override fun onSuccess(response: Page<Worker>) {
                        workers = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("WorkerViewModel", error.message)
                    }
                })
        }
    }

    fun create(onError: (String) -> Unit = {}, onSuccess: () -> Unit = {}) {
        creationLoading = true
        viewModelScope.launch {
            apiEndpoint.workerRepository.create(workerFormVM.toWorkerPayload())
                .enqueue(object : ApiCallback<Worker>() {
                    override fun onSuccess(response: Worker) {
                        creationLoading = false
                        workers?.let {
                            it.content = it.content + (response)
                        }
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        creationLoading = false
                        onError(error.message)
                        Log.d("WorkerViewModel", error.message)
                    }
                })
        }
    }
}