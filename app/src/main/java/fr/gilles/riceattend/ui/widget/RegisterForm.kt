package fr.gilles.riceattend.ui.widget

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.RegisterUser
import fr.gilles.riceattend.ui.formfields.EmailFieldState
import fr.gilles.riceattend.ui.formfields.PasswordFieldState
import fr.gilles.riceattend.ui.formfields.TextFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@Preview
fun RegisterForm(
    viewModel: RegisterFormViewModel = RegisterFormViewModel(),
    additional: @Composable () -> Unit = {},
    onError: (String) -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = "M'Inscrire".uppercase(),
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
                    .padding(top = 8.dp, bottom = 8.dp),
                shape = CircleShape,
                value = viewModel.nameState.value,
                onValueChange = {
                    viewModel.nameState.value = it
                    viewModel.nameState.validate()
                },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.ShortText, contentDescription = "Password Icon")
                        Text("Nom")
                    }
                },
                isError = viewModel.nameState.error != null,
                trailingIcon = {
                },
                singleLine = true
            )
            viewModel.nameState.error?.let {
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
                    .padding(top = 8.dp, bottom = 8.dp),
                shape = CircleShape,
                value = viewModel.surnameState.value,
                onValueChange = {
                    viewModel.surnameState.value = it
                    viewModel.surnameState.validate()
                },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.ShortText, contentDescription = "Password Icon")
                        Text("Prenom")
                    }
                },
                isError = viewModel.surnameState.error != null,
                singleLine = true
            )
            viewModel.surnameState.error?.let { ErrorText(text = it) }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                shape = CircleShape,
                value = viewModel.emailState.value,
                onValueChange = {

                    viewModel.emailState.value = it
                    Log.d("RegisterForm", "onValueChange: ${viewModel.emailState.value}")
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
                    .padding(top = 8.dp, bottom = 8.dp),
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
                        Icon(Icons.Default.Lock, contentDescription = "Password Icon")
                        Text("Mot de passe")
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
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
            )
            viewModel.passwordState.error?.let { ErrorText(text = it) }
            Button(
                onClick = { viewModel.register(onSuccess = onSuccess, onError = onError) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(30.dp),
                enabled = viewModel.emailState.isValid() &&
                        viewModel.surnameState.isValid() &&
                        viewModel.nameState.isValid() &&
                        viewModel.passwordState.isValid() &&
                        !viewModel.loading

            ) {
                if (viewModel.loading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                else
                    Text(text = "S'enregistrer")
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            additional(

            )

        }
    }
}


class RegisterFormViewModel {
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
        CoroutineScope(Dispatchers.IO).launch {
            loading = true
            ApiEndpoint.authRepository.register(
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

