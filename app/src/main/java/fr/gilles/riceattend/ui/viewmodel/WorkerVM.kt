package fr.gilles.riceattend.ui.viewmodel

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.gilles.riceattend.models.WorkerDetails
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
class WorkerVM @AssistedInject constructor(
    @Assisted val code: String,
    private val apiEndpoint: ApiEndpoint
    ) : ViewModel() {
    var loading by mutableStateOf(false)
    var worker by mutableStateOf<WorkerDetails?>(null)
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
            apiEndpoint.workerRepository.get(code)
                .enqueue(object : ApiCallback<WorkerDetails>() {
                    override fun onSuccess(response: WorkerDetails) {
                        worker = response
                        loading = false
                        initUpdateFormViewModel()
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

    fun updateWorker(onFinish: () -> Unit = {}) {
        updateLoading = true
        viewModelScope.launch {
            worker?.let {
                apiEndpoint.workerRepository.update(
                    it.code,
                    workerFormVM.toWorkerPayload()
                ).enqueue(object : ApiCallback<WorkerDetails>() {
                    override fun onSuccess(response: WorkerDetails) {
                        response.activityWorkers = worker?.activityWorkers!!
                        worker = response
                        updateLoading = false
                        DialogService.show(
                            Dialog(
                                title = "Ouvrier mis à jour",
                                message = "Les informations de l'ouvrier ont été mises à jour avec succès",
                                displayDismissButton = false
                            )
                        )
                        onFinish()
                        initUpdateFormViewModel()
                    }

                    override fun onError(error: ApiResponseError) {
                        updateLoading = false
                        DialogService.show(
                            Dialog(
                                title = "Erreur",
                                message = "Une erreur est survenue lors de la mise à jour de l'ouvrier",
                                displayDismissButton = false,
                                icon = Icons.Outlined.Error
                            )
                        )
                    }
                })
            }
        }
    }


    @AssistedFactory
    interface Factory{
        fun create(code: String): WorkerVM
    }



    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            workerViewModel: Factory,
            code: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return workerViewModel.create(code) as T
            }
        }
    }

}