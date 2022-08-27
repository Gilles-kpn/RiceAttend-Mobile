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
import fr.gilles.riceattend.services.entities.models.Activity
import fr.gilles.riceattend.services.entities.models.ActivityParam
import fr.gilles.riceattend.services.entities.models.Page
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class ActivitiesVM : ViewModel() {
    var activities by mutableStateOf<List<Activity>>(listOf())
    var hasMore by mutableStateOf(false)
    var loading by mutableStateOf(false)
    var loadingMore by mutableStateOf(false)
    val params by mutableStateOf(ActivityParam())
    val searchState by mutableStateOf(
        TextFieldState(
            validator = { it.isNotBlank() && it.isNotEmpty() },
            errorMessage = { "Valeur a rechercher requis" },
            defaultValue = ""
        )
    )

    init {
        loadActivities()
    }


    private fun loadActivities() {
        loading = true
        load { loading = false }
    }


    private fun load(onFinish: () -> Unit = {})  =  viewModelScope.launch {
        ApiEndpoint.activityRepository.getActivities(params.toMap(), params.status)
            .enqueue(object : ApiCallback<Page<Activity>>() {
                override fun onSuccess(response: Page<Activity>) {
                    hasMore = !response.last!!
                    activities = activities.plus(response.content)
                    onFinish()
                }

                override fun onError(error: ApiResponseError) {
                    onFinish()
                    Log.d("ActivitiesViewModel", error.message)
                }

            })
    }

    fun viewMore() {
        loadingMore = true
        params.pageNumber += 1
        load {
            loadingMore = false
        }

    }

}

