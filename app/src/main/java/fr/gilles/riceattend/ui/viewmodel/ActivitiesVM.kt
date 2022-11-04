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
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.Activity
import fr.gilles.riceattend.services.entities.models.ActivityParam
import fr.gilles.riceattend.services.entities.models.Page
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class ActivitiesVM @Inject constructor(
    private val apiEndpoint: ApiEndpoint
) : ViewModel() {
    var activities by mutableStateOf<List<Activity>>(listOf())
    var hasMore by mutableStateOf(false)
    var loading by mutableStateOf(false)
    var loadingMore by mutableStateOf(false)
    val params by mutableStateOf(ActivityParam())


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
        activities = listOf()
        load { loading = false }
    }



    private fun load(onFinish: () -> Unit = {})  =  viewModelScope.launch {
        apiEndpoint.activityRepository.getActivities(params.toMap(), params.status)
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

