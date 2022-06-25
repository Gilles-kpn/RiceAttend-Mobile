package fr.gilles.riceattend.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.services.entities.models.LoginUser
import fr.gilles.riceattend.services.entities.models.User
import fr.gilles.riceattend.ui.formfields.EmailFieldState
import fr.gilles.riceattend.ui.formfields.PasswordFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Preview
@Composable
fun LoginFormWidget(
    viewModel: LoginFormViewModel = LoginFormViewModel(),
    additional: @Composable () -> Unit = {},
    onError: (String) -> Unit = {},
    onSuccess: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp)),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = "Me connecter".uppercase(),
                fontSize = 20.sp,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                shape = CircleShape,
                value = viewModel.emailState.value,
                onValueChange = {
                    viewModel.emailState.value = it
                    viewModel.emailState.validate()
                },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.Email, contentDescription = "Password Icon")
                        Text("Email")
                    }
                },
                isError = viewModel.emailState.error != null,
                trailingIcon = {
//                    Icon(Icons.Default.Email , contentDescription = "Email Icon" )
                },
                singleLine = true
            )
            viewModel.emailState.error?.let {
                ErrorText(text = it)
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                shape = CircleShape,
                value = viewModel.passwordState.value,
                onValueChange = {
                    viewModel.passwordState.value = it
                    viewModel.passwordState.validate()
                },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.Password, contentDescription = "Password Icon")
                        Text("Mot de Passe")
                    }

                },
                isError = viewModel.passwordState.error != null,
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.passwordVisible = !viewModel.passwordVisible
                    }) {
                        if (viewModel.passwordVisible)
                            Icon(Icons.Default.Visibility, contentDescription = "Password Visible")
                        else
                            Icon(
                                Icons.Default.VisibilityOff,
                                contentDescription = "Password Not Visible"
                            )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (viewModel.passwordVisible) KeyboardType.Text else KeyboardType.Password
                ),
                singleLine = true,
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )
            viewModel.passwordState.error?.let {
                ErrorText(text = it)
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
            Button(
                onClick = {
                    viewModel.login(
                        onError, onSuccess
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(30.dp),
                enabled = viewModel.emailState.isValid()
                        && viewModel.passwordState.isValid()
                        && !viewModel.loading

            ) {
                if (viewModel.loading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                else
                    Text(text = "Se Connecter")
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
            additional()
        }
    }

}

class LoginFormViewModel : ViewModel() {
    val emailState by mutableStateOf(EmailFieldState())
    val passwordState by mutableStateOf(PasswordFieldState())
    var passwordVisible by mutableStateOf(false)
    var loading by mutableStateOf(false)


    fun login(onError: (String) -> Unit, onSuccess: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            loading = true
            ApiEndpoint.authRepository.login(
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
        ApiEndpoint.authRepository.current().enqueue(object : ApiCallback<User>() {
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