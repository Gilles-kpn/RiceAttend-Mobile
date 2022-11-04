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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.services.entities.models.ActivityPaddyField
import fr.gilles.riceattend.services.entities.models.ActivityStatus
import fr.gilles.riceattend.services.entities.models.ActivityWithDetails
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ActivityVM @AssistedInject constructor(
    @Assisted val code: String,
    private val apiEndpoint: ApiEndpoint
    ) : ViewModel() {
    var activity by mutableStateOf<ActivityWithDetails?>(null)
    var loading by mutableStateOf(false)

    init {
        loadActivity()
    }


    private fun loadActivity() {
        loading = true
        Log.d("Launch one time scope", "1 launch")
        viewModelScope.launch {
            apiEndpoint.activityRepository.get(code)
                .enqueue(object : ApiCallback<ActivityWithDetails>() {
                    override fun onSuccess(response: ActivityWithDetails) {
                        activity = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }
                })
        }
    }


    fun markAsDone(onSuccess: () -> Unit = {}, onError: () -> Unit) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.doneActivity(activity!!.code)
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
            apiEndpoint.activityRepository.undoneActivity(activity!!.code)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            activity!!.status = ActivityStatus.UNDONE
                            activity!!.activityPaddyFields.forEach {
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
            apiEndpoint.activityRepository.startedActivity(activity!!.code)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            activity!!.status = ActivityStatus.IN_PROGRESS
                            activity!!.activityPaddyFields.forEach {
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
            apiEndpoint.activityRepository.cancelActivity(activity!!.code)
                .enqueue(object : Callback<Void> {

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            activity!!.status = ActivityStatus.INIT
                            activity!!.activityPaddyFields.forEach {
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
            apiEndpoint.activityRepository.delete(activity!!.code)
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

    fun removeActivityPaddyFieldFromActivity(activityPaddyField: ActivityPaddyField, onFinish: () -> Unit = {},onSuccess: () -> Unit = {}, onError: () -> Unit ={} ){
        viewModelScope.launch {
            apiEndpoint.activityRepository.deletePaddyFieldFromActivity(
                activity!!.code,
                activityPaddyField.code
            ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        onSuccess()
                    }else onError()

                    onFinish()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onError()
                }
            })
        }
    }

    fun startActivityOnPaddyField(activityPaddyField: ActivityPaddyField, onFinish: () -> Unit = {}, onSuccess: () -> Unit = {}, onError: () -> Unit ={} ){
        viewModelScope.launch {
            apiEndpoint.activityRepository.startedPaddyField(
                activity!!.code,
                activityPaddyField.code
            ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        onSuccess()
                    }else onError()

                    onFinish()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onError()
                }
            })
        }
    }

    fun undoneActivityOnPaddyField(activityPaddyField: ActivityPaddyField, onFinish: () -> Unit = {}, onSuccess: () -> Unit = {}, onError: () -> Unit ={} ){
        viewModelScope.launch {
            apiEndpoint.activityRepository.undonePaddyField(
                activity!!.code,
                activityPaddyField.code
            ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        onSuccess()
                    }else onError()

                    onFinish()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    onError()
                }
            })
        }
    }

    fun doneActivityOnPaddyField(activityPaddyField: ActivityPaddyField, onFinish: () -> Unit = {}, onSuccess: () -> Unit = {}, onError: () -> Unit ={} ){
        viewModelScope.launch {
            apiEndpoint.activityRepository.donePaddyField(
                activity!!.code,
                activityPaddyField.code
            ).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        onSuccess()
                    }else onError()

                    onFinish()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                   onError()
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
            activityVMFactory: Factory,
            code: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return activityVMFactory.create(code) as T
            }
        }
    }
}