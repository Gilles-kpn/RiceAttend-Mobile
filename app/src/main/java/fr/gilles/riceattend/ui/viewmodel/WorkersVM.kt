package fr.gilles.riceattend.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.models.Page
import fr.gilles.riceattend.models.Params
import fr.gilles.riceattend.models.Worker
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
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
    var loadingMore by mutableStateOf(false)
    var workerFormVM by mutableStateOf(WorkerFormVM())
    var creationLoading by mutableStateOf(false)


    init {
        loading = true
        load(onFinish = {
            loading = false
        })
    }

    fun load(onFinish: ()->Unit = {}) {
        viewModelScope.launch {
            apiEndpoint.workerRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<Worker>>() {
                    override fun onSuccess(response: Page<Worker>) {
                        workers = if(workers == null) response else workers?.content?.let {
                            response.copy(
                                content =
                                it.plus(response.content)
                            )
                        }
                        onFinish()
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
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
                        workers?.content?.let {
                            workers = workers?.copy(
                                content = it.plus(response)
                            )
                        }
                        DialogService.show(Dialog(
                            title = "Ouvrier ajouté",
                            message = "L'ouvrier a été ajouté avec succès",
                            displayDismissButton = false
                        ))
                        workerFormVM = WorkerFormVM()
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

    fun viewMore() {
        if(workers?.last != true){
            loadingMore = true
            params.pageNumber += 1
            load {
                loadingMore = false
            }
        }

    }

    fun refresh() {
        params = Params()
        workers = null
        loading = true
        load {
            loading = false
        }
    }
}