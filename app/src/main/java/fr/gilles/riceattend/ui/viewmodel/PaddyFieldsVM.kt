package fr.gilles.riceattend.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.models.*
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PaddyFieldsVM @Inject constructor(
    private val apiEndpoint: ApiEndpoint
)  : ViewModel() {
    val searchState by mutableStateOf(TextFieldState(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Valeur a rechercher requis"
    }, defaultValue = ""))
    var paddyFields: Page<PaddyField>? by mutableStateOf(null)
    var plants: Page<Plant>? by mutableStateOf(null)
    var queryParams: Params = Params()
    var loading by mutableStateOf(false)
    var loadingMore by mutableStateOf(false)
    val paddyFormViewModel by mutableStateOf(PaddyFieldFormVM())
    var paddyFieldCreationLoading by mutableStateOf(false)

    init {
        loadPaddyField()
    }


    private fun loadPaddyField() {
        loading = true
        viewModelScope.launch {
            load{
                loading = false
            }
        }.also {
            loadPlants()
        }
    }

    private fun loadPlants() {
        viewModelScope.launch {
            apiEndpoint.resourceRepository.getPlant(
                Params(
                    pageNumber = 0,
                    pageSize = 200,
                    sort = Sort.DESC
                ).toMap()
            )
                .enqueue(object : ApiCallback<Page<Plant>>() {
                    override fun onSuccess(response: Page<Plant>) {
                        plants = response
                    }

                    override fun onError(error: ApiResponseError) {
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }
                })
        }
    }


    private fun load(onFinish:()->Unit = {}){
        viewModelScope.launch {
            apiEndpoint.paddyFieldRepository.get(queryParams.toMap())
                .enqueue(object : ApiCallback<Page<PaddyField>>() {
                    override fun onSuccess(response: Page<PaddyField>) {
                        paddyFields = if(paddyFields == null) response else paddyFields?.content?.let {
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

    fun createPaddyField(onError: (String) -> Unit = {}, onSuccess: () -> Unit = {}) {
        paddyFieldCreationLoading = true
        viewModelScope.launch {
            apiEndpoint.paddyFieldRepository.create(paddyFormViewModel.toPaddyFieldPayload())
                .enqueue(object : ApiCallback<PaddyField>() {
                    override fun onSuccess(response: PaddyField) {
                        paddyFieldCreationLoading = false
                        DialogService.show(
                            Dialog(
                                title = "Rizière créée",
                                message = "La rizière a été créée avec succès",
                                displayDismissButton = false,
                            )
                        )
                        paddyFields?.let {
                            paddyFields = it.copy(
                                content = it.content.plus(response)
                            )
                        }
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        paddyFieldCreationLoading = false
                        onError(error.message)
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }
                })
        }
    }

    fun viewMore() {
        if(paddyFields?.last != true){
            loadingMore = true
            queryParams.pageNumber += 1
            load {
                loadingMore = false
            }
        }

    }

    fun refresh() {
        queryParams.pageNumber = 0
        paddyFields = null
        loadPaddyField()
    }

}