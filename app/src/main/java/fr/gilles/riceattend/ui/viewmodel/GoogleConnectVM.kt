package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.models.GoogleUser
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
open class GoogleConnectVM @Inject constructor(
     val apiEndpoint: ApiEndpoint
) : ViewModel() {
    var googleLoading by mutableStateOf(false)
    fun loginWithGoogle(task: Task<GoogleSignInAccount>, onSuccess: () -> Unit = {}) {
        if (task.isSuccessful){
            viewModelScope.launch {
                googleLoading = true
                apiEndpoint.authRepository.loginWithGoogle(task.result.idToken!!).enqueue(object : ApiCallback<GoogleUser>() {
                    override fun onSuccess(response: GoogleUser) {
                        googleLoading = false
                        SessionManager.session.authorization = response.authorization
                        SessionManager.session.user = response.user
                        SessionManager.store()
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        DialogService.show(
                            Dialog(
                                title = "Connexion impossible",
                                message = "Une erreur est survenue lors de la connexion avec Google, Si le problème persiste, veuillez verifier si vous avez bien autorisé l'application à accéder à votre compte Google",
                                icon = Icons.Default.Error,
                                displayDismissButton = false,
                                onSuccess = {
                                    googleLoading = false
                                }
                            )
                        )

                    }
                })
            }
        }else{
            DialogService.show(
                Dialog(
                    title = "Erreur",
                    message = "Une erreur est survenue lors de la connexion avec Google, Veuillez reessayer. Si le probleme persiste, contactez nous.",
                    displayDismissButton = false
                )
            )
        }

    }
}