package fr.gilles.riceattend.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.models.LoginUser
import fr.gilles.riceattend.models.User
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.ui.formfields.EmailFieldState
import fr.gilles.riceattend.ui.formfields.PasswordFieldState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class LoginVM @Inject constructor(apiEndpoint: ApiEndpoint) :
    GoogleConnectVM(apiEndpoint) {
    val emailState by mutableStateOf(EmailFieldState())
    val passwordState by mutableStateOf(PasswordFieldState())
    var passwordVisible by mutableStateOf(false)
    var loading by mutableStateOf(false)


    fun login(onError: (String) -> Unit, onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading = true
            apiEndpoint.authRepository.login(
                LoginUser(emailState.value, passwordState.value)
            ).enqueue(object : ApiCallback<String>() {
                override fun onSuccess(response: String) {
                    loading = false
                    SessionManager.session.authorization = response
                    currentUser(onSuccess, onError)
                }

                override fun onError(error: ApiResponseError) {
                    loading = false
                    onError("Identifiants incorrects ou invalides")
                }
            })
        }
    }

    fun currentUser(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            apiEndpoint.authRepository.current().enqueue(object : ApiCallback<User>() {
                override fun onSuccess(response: User) {
                    SessionManager.session.user = response
                    SessionManager.store()
                    onSuccess()
                }

                override fun onError(error: ApiResponseError) {
                    onError("Une erreur est survenue")
                }

            })
        }

    }


}