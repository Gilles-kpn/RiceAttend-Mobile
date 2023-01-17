package fr.gilles.riceattend.ui.viewmodel

import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Error
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
import fr.gilles.riceattend.models.*
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import kotlinx.coroutines.launch
import java.util.*
import kotlin.streams.toList

@RequiresApi(Build.VERSION_CODES.O)
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


    fun markAsDone() {
        viewModelScope.launch {
            apiEndpoint.activityRepository.doneActivity(code)
                .enqueue(object : ApiCallback<Unit>() {
                    override fun onSuccess(response: Unit) {
                        activity?.let {
                            activity = it.copy(
                                status = ActivityStatus.DONE,
                                activityPaddyFields = it.activityPaddyFields.stream().map { paddy ->
                                    paddy.copy(status = ActivityStatus.DONE)
                                }.toList()
                            )
                        }

                        DialogService.show(
                            Dialog(
                                title = "Activité terminée",
                                message = "L'activité a éte terminée avec succes et également sur toute les rizières concernées",
                                icon = Icons.Outlined.Check,
                                displayDismissButton = false
                            )
                        )
                    }

                    override fun onError(error: ApiResponseError) {
                        DialogService.show(
                            Dialog(
                                title = "Erreur",
                                message = "Il est impossible de terminée cette activité, veuillez réessayer plus tard",
                                icon = Icons.Outlined.Error,
                                displayDismissButton = false
                            )
                        )
                    }

                })
        }
    }


    fun markAsStarted() {
        viewModelScope.launch {
            apiEndpoint.activityRepository.startedActivity(code)
                .enqueue(object : ApiCallback<Unit>() {
                    override fun onSuccess(response: Unit) {
                        activity?.let {
                            activity = it.copy(
                                status = ActivityStatus.IN_PROGRESS,
                                activityPaddyFields = it.activityPaddyFields.stream().map { paddy ->
                                    paddy.copy(status = ActivityStatus.IN_PROGRESS)
                                }.toList()
                            )
                        }

                        DialogService.show(
                            Dialog(
                                title = "Activité débutée",
                                message = "L'activité a éte débutée avec succes et également sur toute les rizières concernées",
                                icon = Icons.Outlined.Check,
                                displayDismissButton = false
                            )
                        )
                    }

                    override fun onError(error: ApiResponseError) {
                        DialogService.show(
                            Dialog(
                                title = "Erreur",
                                message = "Il est impossible de debuter cette activité, veuillez réessayer plus tard",
                                icon = Icons.Outlined.Error,
                                displayDismissButton = false
                            )
                        )
                    }

                })
        }

    }


    fun cancelActivity() {
        viewModelScope.launch {
            apiEndpoint.activityRepository.cancelActivity(code)
                .enqueue(object : ApiCallback<Unit>() {
                    override fun onSuccess(response: Unit) {
                        activity?.let {
                            activity = it.copy(
                                status = ActivityStatus.CANCELLED,
                                activityPaddyFields = it.activityPaddyFields.stream().map { paddy ->
                                    paddy.copy(status = ActivityStatus.CANCELLED)
                                }.toList()
                            )
                        }
                        DialogService.show(
                            Dialog(
                                title = "Activité annulée",
                                message = "L'activité a éte annulée avec succes et également sur toute les rizières concernées",
                                icon = Icons.Outlined.Check,
                                displayDismissButton = false
                            )
                        )
                    }

                    override fun onError(error: ApiResponseError) {
                        DialogService.show(
                            Dialog(
                                title = "Annulation impossible",
                                message = "Il est impossible d'annuler cette activité, veuillez réessayer plus tard",
                                icon = Icons.Outlined.Error,
                                displayDismissButton = false
                            )
                        )
                    }

                })
        }
    }

    fun deleteActivity(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.delete(code)
                .enqueue(object : ApiCallback<Unit>() {
                    override fun onSuccess(response: Unit) {
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        DialogService.show(
                            Dialog(
                                title = "Suppression impossible",
                                message = "Il est impossible de supprimer cette activité, veuillez réessayer plus tard",
                                icon = Icons.Outlined.Error,
                                displayDismissButton = false
                            )
                        )
                    }

                })
        }
    }

    fun plainlyActivityOnAgenda() {
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
                DialogService.show(
                    Dialog(
                        title = "Activité ajoutée",
                        message = "L'activité a éte ajoutée avec succes à votre agenda",
                        icon = Icons.Outlined.Check,
                        displayDismissButton = false
                    )
                )
            }
        }
    }

    fun removeActivityPaddyFieldFromActivity(paddyField: PaddyField) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.deletePaddyFieldFromActivity(
                code,
                paddyField.code
            )
                .enqueue(object : ApiCallback<Unit>() {
                    override fun onSuccess(response: Unit) {
                        activity?.let {
                            activity = it.copy(
                                activityPaddyFields = it.activityPaddyFields.stream()
                                    .filter { paddy ->
                                        paddy.paddyField.code != paddyField.code
                                    }.toList()
                            )
                        }
                        DialogService.show(
                            Dialog(
                                title = "Rizière supprimée",
                                message = "La rizière a éte supprimée avec succes de l'activité",
                                icon = Icons.Outlined.Check,
                                displayDismissButton = false
                            )
                        )
                    }

                    override fun onError(error: ApiResponseError) {
                        DialogService.show(
                            Dialog(
                                title = "Suppression impossible",
                                message = "Il est impossible de supprimer cette rizière de l'activité, veuillez réessayer",
                                displayDismissButton = false
                            )
                        )
                    }
                })
        }

    }

    fun removeWorkerFromActivity(worker: Worker) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.deleteWorkerFromActivity(
                code,
                worker.code
            ).enqueue(object : ApiCallback<Unit>() {
                override fun onSuccess(response: Unit) {
                    DialogService.show(
                        Dialog(
                            title = "Suppression effectuée",
                            message = "Le travailleur a été supprimé avec succes de l'activité",
                            icon = Icons.Outlined.Check,
                            displayDismissButton = false
                        )
                    )
                    activity = activity!!.copy(
                        activityWorkers = activity!!.activityWorkers.filter { it.worker.code != worker.code }
                    )
                }

                override fun onError(error: ApiResponseError) {
                    DialogService.show(
                        Dialog(
                            title = "Suppression impossible",
                            message = "Il est impossible de supprimer ce ouvrier de cette activité, veuillez réessayer",
                            displayDismissButton = false,
                        )
                    )
                }

            })
        }
    }

    fun markAsStartedOnPaddyField(paddyField: PaddyField) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.startedPaddyField(
                code,
                paddyField.code
            ).enqueue(object : ApiCallback<Unit>() {
                override fun onSuccess(response: Unit) {
                    DialogService.show(
                        Dialog(
                            title = "Activité démarrée",
                            message = "L'activité a été démarrée avec succes sur le champ",
                            icon = Icons.Outlined.Check,
                            displayDismissButton = false
                        )
                    )
                    activity = activity!!.copy(
                        activityPaddyFields = activity!!.activityPaddyFields.map {
                            if (it.paddyField.code == paddyField.code) it.copy(
                                status = ActivityStatus.IN_PROGRESS
                            ) else it
                        }
                    )
                }

                override fun onError(error: ApiResponseError) {
                    DialogService.show(
                        Dialog(
                            title = "Démarrage impossible",
                            message = "Il est impossible de démarrer cette activité sur ce champ, veuillez réessayer",
                            displayDismissButton = false,
                        )
                    )
                }

            })
        }
    }

    fun markAsDoneOnPaddyField(paddyField: PaddyField) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.donePaddyField(
                code,
                paddyField.code
            ).enqueue(object : ApiCallback<Unit>() {
                override fun onSuccess(response: Unit) {
                    DialogService.show(
                        Dialog(
                            title = "Activité terminée",
                            message = "L'activité a été terminée avec succes sur le champ",
                            icon = Icons.Outlined.Check,
                            displayDismissButton = false
                        )
                    )
                    activity = activity!!.copy(
                        activityPaddyFields = activity!!.activityPaddyFields.map {
                            if (it.paddyField.code == paddyField.code) it.copy(
                                status = ActivityStatus.DONE
                            ) else it
                        }
                    )
                }

                override fun onError(error: ApiResponseError) {
                    DialogService.show(
                        Dialog(
                            title = "Terminaison impossible",
                            message = "Il est impossible de terminer cette activité sur ce champ, veuillez réessayer",
                            displayDismissButton = false,
                        )
                    )
                }

            })
        }

    }

    fun cancelActivityOnPaddyField(paddyField: PaddyField) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.cancelPaddyField(
                code,
                paddyField.code
            ).enqueue(object : ApiCallback<Unit>() {
                override fun onSuccess(response: Unit) {
                    DialogService.show(
                        Dialog(
                            title = "Activité annulée",
                            message = "L'activité a été annulée avec succes sur le champ",
                            icon = Icons.Outlined.Check,
                            displayDismissButton = false
                        )
                    )
                    activity = activity!!.copy(
                        activityPaddyFields = activity!!.activityPaddyFields.map {
                            if (it.paddyField.code == paddyField.code) it.copy(
                                status = ActivityStatus.CANCELLED
                            ) else it
                        }
                    )
                }

                override fun onError(error: ApiResponseError) {
                    DialogService.show(
                        Dialog(
                            title = "Annulation impossible",
                            message = "Il est impossible d'annuler cette activité sur ce champ, veuillez réessayer",
                            displayDismissButton = false,
                        )
                    )
                }

            })
        }

    }

    fun removeResourceFromActivity(resource: Resource) {
        viewModelScope.launch {
            apiEndpoint.activityRepository.deleteResourceFromActivity(
                code,
                resource.code
            ).enqueue(object : ApiCallback<Unit>() {
                override fun onSuccess(response: Unit) {
                    DialogService.show(
                        Dialog(
                            title = "Suppression effectuée",
                            message = "La ressource a été supprimée avec succes de l'activité",
                            icon = Icons.Outlined.Check,
                            displayDismissButton = false
                        )
                    )
                    activity = activity!!.copy(
                        activityResources = activity!!.activityResources.filter { it.resource.code != resource.code }
                    )
                }

                override fun onError(error: ApiResponseError) {
                    DialogService.show(
                        Dialog(
                            title = "Suppression impossible",
                            message = "Il est impossible de supprimer cette ressource de cette activité, veuillez réessayer",
                            displayDismissButton = false,
                        )
                    )
                }

            })
        }

    }

    fun modifyResourceQuantityOnActivity(resource: Resource) {

    }


    @AssistedFactory
    interface Factory {
        fun create(code: String): ActivityVM
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