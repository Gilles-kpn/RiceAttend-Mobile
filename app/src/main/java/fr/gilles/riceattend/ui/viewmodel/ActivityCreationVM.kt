package fr.gilles.riceattend.ui.viewmodel

import android.os.Build
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ActivityCreationVM @Inject constructor(
    private val apiEndpoint: ApiEndpoint
) : ViewModel() {
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
            apiEndpoint.paddyFieldRepository.get(Params(0, 100).toMap())
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
            apiEndpoint.workerRepository.get(Params(0, 100).toMap())
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
            apiEndpoint.resourceRepository.get(Params(0, 100).toMap())
                .enqueue(object : ApiCallback<Page<Resource>>() {
                    override fun onSuccess(response: Page<Resource>) {
                        resources = response
                    }

                    override fun onError(error: ApiResponseError) {
                    }
                })

        }
    }

    fun createActivity() {
        loading = true
        viewModelScope.launch {
            apiEndpoint.activityRepository.create(activityFormVM.toActivityPayload())
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