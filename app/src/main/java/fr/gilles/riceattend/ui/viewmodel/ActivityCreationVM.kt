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
import fr.gilles.riceattend.services.entities.models.*
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ActivityCreationVM : ViewModel() {
    val activityFormVM by mutableStateOf(ActivityFormVM())
    var paddyFields by mutableStateOf<Page<PaddyField>?>(null)
    var workers by mutableStateOf<Page<Worker>?>(null)
    var resources by mutableStateOf<Page<Resource>?>(null)
    var loading by mutableStateOf(false)
    var createdActivity by mutableStateOf<Activity?>(null)


    init {
        loadPaddyField()
        loadWorkers()
        loadResources()
    }

    fun loadPaddyField() {
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.get(Params(0, 100).toMap())
                .enqueue(object : ApiCallback<Page<PaddyField>>() {
                    override fun onSuccess(response: Page<PaddyField>) {
                        paddyFields = response
                    }

                    override fun onError(error: ApiResponseError) {

                    }
                })
        }
    }

    fun loadWorkers() {
        viewModelScope.launch {
            ApiEndpoint.workerRepository.get(Params(0, 100).toMap())
                .enqueue(object : ApiCallback<Page<Worker>>() {
                    override fun onSuccess(response: Page<Worker>) {
                        workers = response
                    }

                    override fun onError(error: ApiResponseError) {

                    }
                })
        }
    }

    fun loadResources() {
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.get(Params(0, 100).toMap())
                .enqueue(object : ApiCallback<Page<Resource>>() {
                    override fun onSuccess(response: Page<Resource>) {
                        resources = response
                    }

                    override fun onError(error: ApiResponseError) {
                        TODO("Not yet implemented")
                    }
                })

        }
    }

    fun createActivity() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.activityRepository.create(activityFormVM.toActivityPayload())
                .enqueue(object : ApiCallback<Activity>() {
                    override fun onSuccess(response: Activity) {
                        createdActivity = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }
                })
        }
    }

}