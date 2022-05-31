package fr.gilles.riceattend.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gilles.riceattend.ui.formfields.EmailFieldState
import fr.gilles.riceattend.ui.formfields.PasswordFieldState
import fr.gilles.riceattend.ui.formfields.TextFieldState

@Composable
@Preview
fun RegisterForm(
    viewModel: RegisterFormViewModel = RegisterFormViewModel(),
    additional: @Composable () -> Unit = {}
){
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = "M'Inscrire".uppercase(),
                fontSize = 20.sp,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 10.dp,end = 10.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                shape = CircleShape ,
                value = viewModel.nameState.text,
                onValueChange = {
                    viewModel.nameState.text = it;
                    viewModel.nameState.validate()
                },
                label = {
                    Text("Nom")
                },
                isError = viewModel.nameState.error != null,
                trailingIcon = {
//                    Icon(Icons.Default.Email , contentDescription = "Email Icon" )
                },
                singleLine =true
            )
            viewModel.surnameState.error?.let {
                ErrorText(text = it)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(5.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                shape = CircleShape ,
                value = viewModel.surnameState.text,
                onValueChange = {
                    viewModel.surnameState.text = it;
                    viewModel.surnameState.validate()
                },
                label = {
                    Text("Prenom")
                },
                isError = viewModel.surnameState.error != null,
                trailingIcon = {
//                    Icon(Icons.Default.Email , contentDescription = "Email Icon" )
                },
                singleLine =true
            )
            viewModel.surnameState.error?.let {
                ErrorText(text = it)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(5.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                shape = CircleShape ,
                value = viewModel.emailState.text,
                onValueChange = {
                    viewModel.emailState.text = it;
                    viewModel.emailState.validate()
                },
                label = {
                    Text("Email")
                },
                isError = viewModel.emailState.error != null,
                trailingIcon = {
//                    Icon(Icons.Default.Email , contentDescription = "Email Icon" )
                },
                singleLine =true
            )
            viewModel.emailState.error?.let {
                ErrorText(text = it)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(5.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                shape = CircleShape ,
                value = viewModel.passwordState.text,
                onValueChange = {
                    viewModel.passwordState.text = it;
                    viewModel.passwordState.validate()
                },
                label = {
                    Text("Mot de Passe")
                },
                isError = viewModel.passwordState.error != null,
                trailingIcon = {
                    IconButton(onClick = { viewModel.passwordVisible = !viewModel.passwordVisible}) {
                        if(viewModel.passwordVisible)
                            Icon(Icons.Default.Visibility , contentDescription = "Password Visible" )
                        else
                            Icon(Icons.Default.VisibilityOff, contentDescription = "Password Not Visible")
                    }
                },
                singleLine =true
            )
            viewModel.passwordState.error?.let {
                ErrorText(text = it)
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(30.dp),
            ) {
                Text(text = "S'enregistrer")
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(10.dp))
            additional(
            )

        }
    }
}


class RegisterFormViewModel(){
    val emailState by mutableStateOf(EmailFieldState())
    val passwordState by mutableStateOf(PasswordFieldState())
    var passwordVisible by  mutableStateOf(false)
    val nameState by mutableStateOf(TextFieldState(
        validator = {
            it.isNotBlank() && it.isNotEmpty()
        },
        errorMessage = {
            "Nom requis"
        }
    ))
    val surnameState by mutableStateOf(TextFieldState(
        validator = {
            it.isNotBlank() && it.isNotEmpty()
        },
        errorMessage = {
            "Prenom requis"
        }
    ))
}

