package fr.gilles.riceattend.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.Page
import fr.gilles.riceattend.services.entities.models.Params
import fr.gilles.riceattend.services.entities.models.Resource
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ResourcesVM : ViewModel() {
    var resources by mutableStateOf<Page<Resource>?>(null)
    var params by mutableStateOf(Params())
    var loading by mutableStateOf(false)
    var searchState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { true },
            errorMessage = { "" },
        )
    )
    val resourceFormVM by mutableStateOf(ResourceFormVM())


    init {
        loadResources()
    }

    private fun loadResources() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<Resource>>() {
                    override fun onSuccess(response: Page<Resource>) {
                        resources = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }
                })
        }

    }

    fun create(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        resourceFormVM.loading = true
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.create(resourceFormVM.toResourcePayload())
                .enqueue(object : ApiCallback<Resource>() {
                    override fun onSuccess(response: Resource) {
                        resources?.let { it.content = it.content + response }
                        resourceFormVM.loading = false
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        resourceFormVM.loading = false
                        onError(error.message)
                    }
                })
        }
    }
}