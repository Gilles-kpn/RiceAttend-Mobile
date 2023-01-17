package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.models.Page
import fr.gilles.riceattend.models.Params
import fr.gilles.riceattend.models.Resource
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResourcesVM @Inject constructor(
    private val apiEndpoint: ApiEndpoint
)  : ViewModel() {
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
    var loadingMore by mutableStateOf(false)
    val resourceFormVM by mutableStateOf(ResourceFormVM())


    init {
        loadResources()
    }

    private fun loadResources() {
        loading = true
        load{
            loading = false
        }

    }

    private fun load(onFinish: ()-> Unit  = {}){
        viewModelScope.launch {
            apiEndpoint.resourceRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<Resource>>() {
                    override fun onSuccess(response: Page<Resource>) {
                        resources =if(resources == null) response else resources?.content?.let {
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

    fun create(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        resourceFormVM.loading = true
        viewModelScope.launch {
            apiEndpoint.resourceRepository.create(resourceFormVM.toResourcePayload())
                .enqueue(object : ApiCallback<Resource>() {
                    override fun onSuccess(response: Resource) {
                        resourceFormVM.loading = false
                        DialogService.show(
                            Dialog(
                                title = "Ressource ajoutée",
                                message = "La ressource a été ajoutée avec succès",
                                displayDismissButton = false,
                            )
                        )
                        resources?.content?.let {
                            resources = resources?.copy(
                                content = it.plus(response)
                            )
                        }
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        resourceFormVM.loading = false
                        DialogService.show(
                            Dialog(
                                title = "Erreur",
                                message = "Une erreur est survenue lors de l'ajout de la ressource",
                                displayDismissButton = false,
                                icon = Icons.Outlined.Error
                            )
                        )
                    }
                })
        }
    }

    fun viewMore() {
        if(resources?.last != true){
            loadingMore = true
            params.pageNumber += 1
            load {
                loadingMore = false
            }
        }
    }

    fun refresh() {
        params.pageNumber = 0
        resources = null
        loadResources()
    }
}