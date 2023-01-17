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
import fr.gilles.riceattend.models.Activity
import fr.gilles.riceattend.models.ActivityParam
import fr.gilles.riceattend.models.Page
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class ActivitiesVM @Inject constructor(
    private val apiEndpoint: ApiEndpoint
) : ViewModel() {
    var activities by mutableStateOf<Page<Activity>?>(null)
    var loading by mutableStateOf(false)
    var loadingMore by mutableStateOf(false)
    var params by mutableStateOf(ActivityParam())
    var name by mutableStateOf(TextFieldState(
        defaultValue = "",
        validator = {
           true
        },
        errorMessage = {
            "Nom requis"
        }
    ))



    init {
        loadActivities()
    }


    private fun loadActivities() {
        loading = true
        load { loading = false }
    }

     fun refresh(){
        params.pageNumber = 0
        loading = true
        load { loading = false }
    }



    private fun load(onFinish: () -> Unit = {})  =  viewModelScope.launch {
     viewModelScope.launch {
         apiEndpoint.activityRepository.getActivities(params.toMap(), params.status.map { e-> e.value })
             .enqueue(object : ApiCallback<Page<Activity>>() {
                 override fun onSuccess(response: Page<Activity>) {
                     activities = if(activities == null) response else activities?.content?.let {
                         response.copy(
                             content =
                             it.plus(response.content)
                         )
                     }
                     onFinish()
                 }

                 override fun onError(error: ApiResponseError) {
                     onFinish()
                     Log.d("ActivitiesViewModel", error.message)
                 }

             })
     }
    }

    fun viewMore() {
        if(activities?.last != true){
            loadingMore = true
            params.pageNumber += 1
            load {
                loadingMore = false
            }
        }

    }

    fun filter() {
        params.name = name.value
        activities = null
        refresh()
    }

}

