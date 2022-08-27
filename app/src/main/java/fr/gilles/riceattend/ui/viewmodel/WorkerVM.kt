package fr.gilles.riceattend.ui.viewmodel

import android.content.Intent
import android.net.Uri
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
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.services.entities.models.ActivityWorker
import fr.gilles.riceattend.services.entities.models.Worker
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class WorkerVM(val code: String) : ViewModel() {
    var loading by mutableStateOf(false)
    var worker by mutableStateOf<Worker?>(null)
    var workerActivities by mutableStateOf<List<ActivityWorker>>(listOf())
    var workerFormVM by mutableStateOf(WorkerFormVM())
    var updateLoading by mutableStateOf(false)

    init {
        getWorker()
    }

    fun openDialer() {
        worker?.let {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phone}"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            SessionManager.context.get()?.startActivity(intent)
        }
    }

    fun openMail() {
        worker?.let {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${it.email}"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            SessionManager.context.get()?.startActivity(intent)
        }
    }

    private fun getWorker() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.workerRepository.get(code)
                .enqueue(object : ApiCallback<Worker>() {
                    override fun onSuccess(response: Worker) {
                        worker = response
                        loading = false
                        initUpdateFormViewModel()
                        loadWorkerActivities()
                        Log.d("WorkerViewModel", "Worker: ${response}")

                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("WorkerViewModel", error.message)
                    }

                })
        }
    }

    private fun initUpdateFormViewModel() {
        worker?.let {
            workerFormVM.firstNameState.value = it.firstName
            workerFormVM.lastNameState.value = it.lastName
            workerFormVM.phoneState.value = it.phone
            workerFormVM.emailState.value = it.email
            it.address.let { address ->
                workerFormVM.addressCityState.value =
                    if (address.city.isNullOrEmpty()) "" else address.city!!
                workerFormVM.addressStreetState.value =
                    if (address.street.isNullOrEmpty()) "" else address.street!!
                workerFormVM.addressCountryState.value =
                    if (address.country.isNullOrEmpty()) "" else address.country!!
            }
            workerFormVM.hourlyPayState.value = it.hourlyPay
        }
    }

    fun updateWorker(onError: (String) -> Unit) {
        updateLoading = true
        viewModelScope.launch {
            worker?.let {
                ApiEndpoint.workerRepository.update(
                    it.code,
                    workerFormVM.toWorkerPayload()
                ).enqueue(object : ApiCallback<Worker>() {
                    override fun onSuccess(response: Worker) {
                        worker = response
                        updateLoading = false
                        initUpdateFormViewModel()
                    }

                    override fun onError(error: ApiResponseError) {
                        updateLoading = false
                        Log.d("WorkerViewModel", error.message)
                    }
                })
            }
        }
    }

    private fun loadWorkerActivities() {
        worker?.let {
            loading = true
            viewModelScope.launch {
                ApiEndpoint.workerRepository.getWorkerActivities(it.code)
                    .enqueue(object : ApiCallback<List<ActivityWorker>>() {
                        override fun onSuccess(response: List<ActivityWorker>) {
                            workerActivities = response
                            loading = false
                            Log.d("WorkerViewModel", "Activities: ${response}")
                        }

                        override fun onError(error: ApiResponseError) {
                            loading = false
                            Log.d("WorkerViewModel", error.message)
                        }

                    })
            }
        }
    }

}