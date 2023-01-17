package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.gilles.riceattend.models.AddQuantity
import fr.gilles.riceattend.models.ResourceDetails
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.launch

class RessourceVM  @AssistedInject constructor(
    @Assisted val code: String,
    private val apiEndpoint: ApiEndpoint
) : ViewModel() {
    var resource by mutableStateOf<ResourceDetails?>(null)
    var loading by mutableStateOf(false)
    var addLoading by mutableStateOf(false)
    var quantityToAdd =  TextFieldState<Int>(
        defaultValue = 0,
        errorMessage = { "Entrez une valeur positive" },
        validator = {
            it > 0;
        }
    )

    init {
        loadRessource()
    }

    fun loadRessource(){
        loading = true
        viewModelScope.launch {
            apiEndpoint.resourceRepository.get(code)
                .enqueue(object : ApiCallback<ResourceDetails>(){
                    override fun onSuccess(response: ResourceDetails) {
                        resource = response;
                        loading = false;
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }

                })
        }
    }

    fun addQuantity(onSuccess:()->Unit = {}, onError:()->Unit = {}) {
        addLoading = true
        viewModelScope.launch {
            apiEndpoint.resourceRepository.addQuantity(resource!!.code,  AddQuantity(quantityToAdd.value.toLong()))
                .enqueue(object : ApiCallback<Unit>(){
                    override fun onSuccess(response: Unit) {
                        addLoading = false
                        resource!!.quantity += quantityToAdd.value
                        quantityToAdd.value = 0
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        addLoading = false
                        onError()
                    }

                })
        }
    }

    @AssistedFactory
    interface Factory{
        fun create(code: String): RessourceVM
    }



    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            factory: Factory,
            code: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(code) as T
            }
        }
    }
}