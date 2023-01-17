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
import fr.gilles.riceattend.models.RegisterUser
import fr.gilles.riceattend.ui.formfields.EmailFieldState
import fr.gilles.riceattend.ui.formfields.PasswordFieldState
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class RegisterVM@Inject constructor(apiEndpoint: ApiEndpoint) :
    GoogleConnectVM(apiEndpoint)  {
    val emailState by mutableStateOf(EmailFieldState())
    val passwordState by mutableStateOf(PasswordFieldState())
    var passwordVisible by mutableStateOf(false)
    val nameState by mutableStateOf(TextFieldState<String>(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Nom requis"
    }, defaultValue = ""))
    val surnameState by mutableStateOf(TextFieldState<String>(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Prenom requis"
    }, defaultValue = ""))
    var loading by mutableStateOf(false)



    fun register(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            loading = true
            apiEndpoint.authRepository.register(
                RegisterUser(
                    email = emailState.value,
                    password = passwordState.value,
                    firstName = nameState.value,
                    lastName = surnameState.value,
                    name = nameState.value + " " + surnameState.value,
                )
            ).enqueue(object : ApiCallback<String>() {
                override fun onSuccess(response: String) {
                    loading = false
                    onSuccess()
                }

                override fun onError(error: ApiResponseError) {
                    loading = false
                    onError("Une erreur est survenue\n${error.message}")
                }
            })
        }
    }


}