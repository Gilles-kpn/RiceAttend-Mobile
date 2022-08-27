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
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class PaddyFieldVM(val code: String) : ViewModel() {
    var loading by mutableStateOf(false)
    var paddyField by mutableStateOf<PaddyField?>(null)
    var plants by mutableStateOf<Page<Plant>?>(null)
    var paddyfieldFormVM by mutableStateOf(PaddyFieldFormVM())
    var updateLoading by mutableStateOf(false)
    var paddyFieldActivities by mutableStateOf<List<ActivityPaddyField>>(listOf())


    init {
        loadPaddyField()
        loadPlants()
    }

     private fun loadPaddyField() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.get(code)
                .enqueue(object : ApiCallback<PaddyField>() {
                    override fun onSuccess(response: PaddyField) {
                        paddyField = response
                        initUpdateForm()
                        loadPaddyFieldActivities()
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }

                })
        }
    }

    private fun initUpdateForm() {
        paddyField?.let {
            paddyfieldFormVM.name.value = it.name
            paddyfieldFormVM.surface_unit.value = it.surface.unit
            paddyfieldFormVM.surface_value.value = it.surface.value.toInt()
            paddyfieldFormVM.numberOfPlants.value = it.numberOfPlants
            paddyfieldFormVM.plant.value = it.plant
            paddyfieldFormVM.description.value =
                if (it.description.isNullOrEmpty()) "" else it.description!!
        }
    }


    fun update(onError: (String) -> Unit) {
        updateLoading = true
        viewModelScope.launch {
            paddyField?.let {
                ApiEndpoint.paddyFieldRepository.update(
                    it.code,
                    paddyfieldFormVM.toPaddyFieldPayload()
                )
                    .enqueue(object : ApiCallback<PaddyField>() {
                        override fun onSuccess(response: PaddyField) {
                            paddyField = response
                            updateLoading = false
                            initUpdateForm()
                        }

                        override fun onError(error: ApiResponseError) {
                            updateLoading = false
                            Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                        }

                    })
            }
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


    private fun loadPaddyFieldActivities(){
        paddyField?.let {
            viewModelScope.launch {
                ApiEndpoint.paddyFieldRepository.getPaddyFieldActivities(it.code)
                    .enqueue(object : ApiCallback<List<ActivityPaddyField>>() {
                        override fun onSuccess(response: List<ActivityPaddyField>) {
                            paddyFieldActivities = response
                            loading = false
                        }

                        override fun onError(error: ApiResponseError) {
                            Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                        }
                    })
            }
        }
    }
}