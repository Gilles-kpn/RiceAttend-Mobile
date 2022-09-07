package fr.gilles.riceattend.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class PaddyFieldsVM : ViewModel() {
    val searchState by mutableStateOf(TextFieldState(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Valeur a rechercher requis"
    }, defaultValue = ""))
    var paddyFields: Page<PaddyField>? by mutableStateOf(null)
    var plants: Page<Plant>? by mutableStateOf(null)
    var queryParams: Params = Params()
    var loading by mutableStateOf(false)
    val paddyFormViewModel by mutableStateOf(PaddyFieldFormVM())
    var paddyFieldCreationLoading by mutableStateOf(false)

    init {
        loadPaddyField()
    }


    private fun loadPaddyField() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.get(queryParams.toMap())
                .enqueue(object : ApiCallback<Page<PaddyField>>() {
                    override fun onSuccess(response: Page<PaddyField>) {
                        paddyFields = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }
                })
        }.also {
            loadPlants()
        }
    }

    private fun loadPlants() {
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.getPlant(
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

    fun createPaddyField(onError: (String) -> Unit = {}, onSuccess: () -> Unit = {}) {
        paddyFieldCreationLoading = true
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.create(paddyFormViewModel.toPaddyFieldPayload())
                .enqueue(object : ApiCallback<PaddyField>() {
                    override fun onSuccess(response: PaddyField) {
                        paddyFieldCreationLoading = false
                        onSuccess()
                        paddyFields?.let {
                            it.content = it.content + response
                        }
                    }

                    override fun onError(error: ApiResponseError) {
                        paddyFieldCreationLoading = false
                        onError(error.message)
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }
                })
        }
    }

}