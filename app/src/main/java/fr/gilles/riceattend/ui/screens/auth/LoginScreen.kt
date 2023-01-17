package fr.gilles.riceattend.ui.screens.auth

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import fr.gilles.riceattend.R
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.viewmodel.LoginVM
import fr.gilles.riceattend.ui.widget.ErrorText
import fr.gilles.riceattend.ui.widget.components.IncludeLottieFile
import fr.gilles.riceattend.utils.Dialog
import fr.gilles.riceattend.utils.DialogService
import fr.gilles.riceattend.utils.getGoogleLoginAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    nav: NavController,
    viewModel: LoginVM = hiltViewModel(),
) {
    val context = LocalContext.current
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
                    viewModel.loginWithGoogle(task) {
                        nav.navigate(Route.MainRoute.path) {
                            popUpTo(Route.LoginRoute.path) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            painter = painterResource(id = R.mipmap.riceattend_logo_text_foreground),
            contentDescription = "Logo",
            modifier = Modifier.clip(CircleShape),
        )
        IncludeLottieFile(
            draw = R.raw.login,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(300.dp)
        )

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
                horizontalAlignment = Alignment.Start,
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
                        .padding(top = 8.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    value = viewModel.emailState.value,
                    onValueChange = {
                        viewModel.emailState.value = it
                        viewModel.emailState.validate()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Password Icon",
                                Modifier
                                    .padding(horizontal = 5.dp)
                                    .size(20.dp)
                            )
                            Text("Email")
                        }
                    },
                    isError = viewModel.emailState.error != null,
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
                    shape = RoundedCornerShape(10.dp),
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
                            Icon(
                                Icons.Default.Password,
                                contentDescription = "Password Icon",
                                Modifier
                                    .padding(horizontal = 5.dp)
                                    .size(20.dp)
                            )
                            Text("Mot de Passe")
                        }

                    },
                    isError = viewModel.passwordState.error != null,
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.passwordVisible = !viewModel.passwordVisible
                        }) {
                            if (viewModel.passwordVisible)
                                Icon(
                                    Icons.Default.Visibility,
                                    contentDescription = "Password Visible"
                                )
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
                            onError = {
                                DialogService.show(
                                    Dialog(
                                        title = "Impossible de se connecter",
                                        message = "Veuillez vérifier vos identifiants, Si vous ne vous souvenez plus de votre mot de passe, veuillez cliquer sur le bouton de réinitialisation du mot de passe, afin de recevoir un mail de réinitialisation",
                                        icon = Icons.Default.Error,
                                        displayDismissButton = false
                                    )
                                )
                            }, onSuccess = {
                                nav.navigate(Route.MainRoute.path) {
                                    popUpTo(Route.LoginRoute.path) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(6.dp),
                    enabled = viewModel.emailState.isValid()
                            && viewModel.passwordState.isValid()
                            && !viewModel.loading

                ) {
                    if (viewModel.loading)
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    else
                        Text(text = "Se Connecter")
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                )
                Text(
                    buildAnnotatedString {
                        append("Vous n'avez pas de compte ? ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                            append("S'inscrire")
                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            nav.navigate(Route.RegisterRoute.path) {
                                popUpTo(Route.LoginRoute.path) {
                                    inclusive = true
                                }
                            }
                        }
                        .padding(10.dp),

                    )
                Button(
                    onClick = {
                        startForResult.launch(getGoogleLoginAuth(context).signInIntent)
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(6.dp),

                    ) {
                    if(viewModel.googleLoading)
                        CircularProgressIndicator()
                    else{
                        Image(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = "",
                            Modifier
                                .size(30.dp)
                                .padding(end = 10.dp)
                        )
                        Text(
                            text = "Se Connecter avec Google",
                            color = Color.White
                        )
                    }

                }
            }
        }
    }
}