package fr.gilles.riceattend.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import kotlinx.coroutines.launch




@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class PaddyFieldVM @AssistedInject constructor(
    @Assisted val code: String,
    private val apiEndpoint: ApiEndpoint
    ) : ViewModel() {
    var loading by mutableStateOf(false)
    var paddyField by mutableStateOf<PaddyFieldDetails?>(null)
    var plants by mutableStateOf<Page<Plant>?>(null)
    var paddyfieldFormVM by mutableStateOf(PaddyFieldFormVM())
    var updateLoading by mutableStateOf(false)


    init {
        loadPaddyField()
        loadPlants()
    }

     private fun loadPaddyField() {
        loading = true
        viewModelScope.launch {
            apiEndpoint.paddyFieldRepository.get(code)
                .enqueue(object : ApiCallback<PaddyFieldDetails>() {
                    override fun onSuccess(response: PaddyFieldDetails) {
                        paddyField = response
                        loading = false;
                        initUpdateForm()
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
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
            paddyField?.let { apiEndpoint.paddyFieldRepository.update(
                    it.code,
                    paddyfieldFormVM.toPaddyFieldPayload()
                )
                    .enqueue(object : ApiCallback<PaddyFieldDetails>() {
                        override fun onSuccess(response: PaddyFieldDetails) {
                            response.activityPaddyFields = it.activityPaddyFields
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

    @AssistedFactory
    interface Factory{
        fun create(code: String): PaddyFieldVM
    }



    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            paddyFieldFactory: Factory,
            code: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return paddyFieldFactory.create(code) as T
            }
        }
    }
}