package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.models.UpdatePassword
import fr.gilles.riceattend.ui.formfields.PasswordFieldState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountVM  @Inject constructor(
    private val apiEndpoint: ApiEndpoint
) : ViewModel(){
    val passwordState by mutableStateOf(PasswordFieldState())
    val oldPasswordState by mutableStateOf(PasswordFieldState())
    var passwordChangeLoading by mutableStateOf(false)





    fun changePassword(callback : ()-> Unit = {}, fallback :()-> Unit = {}) {
        if(passwordState.isValid() && oldPasswordState.isValid()){
            passwordChangeLoading = true
            viewModelScope.launch {
                apiEndpoint.authRepository.updatePassword(
                    UpdatePassword(
                        password = oldPasswordState.value,
                        newPassword = passwordState.value
                    )
                ).enqueue(object : ApiCallback<String>(){
                    override fun onSuccess(response: String) {
                        callback()
                        passwordChangeLoading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        fallback()
                        passwordChangeLoading = false
                    }

                })
            }
        }
    }
}