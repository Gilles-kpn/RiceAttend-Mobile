package fr.gilles.riceattend.ui.viewmodel

import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.services.entities.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class ActivityVM(val code: String) : ViewModel() {
    var activity by mutableStateOf<Activity?>(null)
    var loading by mutableStateOf(false)
    var activityResources by mutableStateOf<List<ActivityResource>>(listOf())
    var activityWorkers by mutableStateOf<List<ActivityWorker>>(listOf())
    var activityPaddyFields by mutableStateOf<List<ActivityPaddyField>>(listOf())

    var selectedActivityResources by mutableStateOf<List<ActivityResource>>(listOf())
    var selectedActivityWorkers by mutableStateOf<List<ActivityWorker>>(listOf())
    var selectedActivityPaddyFields by mutableStateOf<List<ActivityPaddyField>>(listOf())

    init {
        loadActivity()
    }


    private fun loadActivity() {
        loading = true
        Log.d("Launch one time scope", "1 launch")
        viewModelScope.launch {
            ApiEndpoint.activityRepository.get(code)
                .enqueue(object : ApiCallback<Activity>() {
                    override fun onSuccess(response: Activity) {
                        activity = response
                        getActivityPaddyFields()
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }
                })
        }
    }

    private fun getActivityResources() {
        activity?.let {
            viewModelScope.launch {
                ApiEndpoint.activityRepository.getActivityResources(it.code)
                    .enqueue(object : ApiCallback<List<ActivityResource>>() {
                        override fun onSuccess(response: List<ActivityResource>) {
                            activityResources = response
                            getActivityWorkers()
                        }

                        override fun onError(error: ApiResponseError) {
                        }

                    })
            }
        }

    }

    private fun getActivityWorkers() {
        activity?.let {
            viewModelScope.launch {
                ApiEndpoint.activityRepository.getActivityWorkers(it.code)
                    .enqueue(object : ApiCallback<List<ActivityWorker>>() {
                        override fun onSuccess(response: List<ActivityWorker>) {
                            activityWorkers = response
                            loading = false
                        }

                        override fun onError(error: ApiResponseError) {
                        }

                    })
            }
        }

    }


    private fun getActivityPaddyFields() {
        activity?.let {
            viewModelScope.launch {
                ApiEndpoint.activityRepository.getActivityPaddyFields(it.code)
                    .enqueue(object : ApiCallback<List<ActivityPaddyField>>() {
                        override fun onSuccess(response: List<ActivityPaddyField>) {
                            activityPaddyFields = response
                        }

                        override fun onError(error: ApiResponseError) {
                        }

                    })
            }.also {
                getActivityResources()
            }
        }

    }

    fun markAsDone(onSuccess: () -> Unit = {}, onError: () -> Unit) {
        viewModelScope.launch {
            ApiEndpoint.activityRepository.doneActivity(activity!!.code)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful)
                            onSuccess()
                        else
                            onError()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        onError()
                    }
                })
        }
    }

    fun markAsUnDone(onSuccess: () -> Unit = {}, onError: () -> Unit) {
        viewModelScope.launch {
            ApiEndpoint.activityRepository.undoneActivity(activity!!.code)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            activity!!.status = ActivityStatus.UNDONE
                            activityPaddyFields.forEach {
                                it.status = ActivityStatus.UNDONE
                            }
                            onSuccess()
                        } else onError()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        onError()
                    }
                })
        }

    }

    fun markAsStarted(onSuccess: () -> Unit = {}, onError: () -> Unit) {
        viewModelScope.launch {
            ApiEndpoint.activityRepository.startedActivity(activity!!.code)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            activity!!.status = ActivityStatus.IN_PROGRESS
                            activityPaddyFields.forEach {
                                it.status = ActivityStatus.IN_PROGRESS
                            }
                            onSuccess()
                        } else onError()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        onError()
                    }
                })
        }
    }


    fun cancelActivity(onSuccess: () -> Unit = {}, onError: () -> Unit) {
        viewModelScope.launch {
            ApiEndpoint.activityRepository.cancelActivity(activity!!.code)
                .enqueue(object : Callback<Void> {

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            activity!!.status = ActivityStatus.INIT
                            activityPaddyFields.forEach {
                                it.status = ActivityStatus.INIT
                            }
                            onSuccess()
                        } else onError()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        onError()
                    }
                })
        }
    }

    fun deleteActivity(onSuccess: () -> Unit = {}, onError: () -> Unit) {
        viewModelScope.launch {
            ApiEndpoint.activityRepository.delete(activity!!.code)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            onSuccess()
                        } else onError()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        onError()
                    }
                })
        }
    }

    fun plainlyActivityOnAgenda(onSuccess: () -> Unit = {}, onError: () -> Unit) {
        // make a rappel for the activity on the agenda of phone
        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.Events.TITLE, activity!!.name)
        intent.putExtra(CalendarContract.Events.DESCRIPTION, activity!!.description)
        //use system timezone
        intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        intent.putExtra(CalendarContract.Events.ALL_DAY, true)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, activity!!.startDate.time)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, activity!!.endDate.time)
        intent.putExtra(
            CalendarContract.Events.AVAILABILITY,
            CalendarContract.Events.AVAILABILITY_BUSY
        )
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=10")
        intent.putExtra(CalendarContract.Events.CALENDAR_ID, 1)
        intent.putExtra(CalendarContract.Events.EVENT_COLOR, Color.Green.toArgb())
        intent.putExtra(CalendarContract.Events.EVENT_COLOR_KEY, "green")

        // launch intent to create event
        activity?.let {
            SessionManager.context.get()?.startActivity(intent).run {
                onSuccess()
            }
        }
    }

    fun removeActivityPaddyFieldFromActivity(activityPaddyField: ActivityPaddyField){
        viewModelScope.launch {
            ApiEndpoint.activityRepository.deletePaddyFieldFromActivity(
                activity!!.code,
                activityPaddyField.code
            )
        }
    }

    fun startActivityOnPaddyField(activityPaddyField: ActivityPaddyField){
        viewModelScope.launch {
            ApiEndpoint.activityRepository.startedPaddyField(
                activity!!.code,
                activityPaddyField.code
            )
        }
    }

    fun undoneActivityOnPaddyField(activityPaddyField: ActivityPaddyField){
        viewModelScope.launch {
            ApiEndpoint.activityRepository.undonePaddyField(
                activity!!.code,
                activityPaddyField.code
            )
        }
    }

    fun doneActivityOnPaddyField(activityPaddyField: ActivityPaddyField){
        viewModelScope.launch {
            ApiEndpoint.activityRepository.donePaddyField(
                activity!!.code,
                activityPaddyField.code
            )
        }
    }



}