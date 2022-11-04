package fr.gilles.riceattend.ui.screens.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.RegisterForm
import fr.gilles.riceattend.ui.widget.components.OpenDialog
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(nav: NavController, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    var alert by remember { mutableStateOf(false) }
    OpenDialog(
        title = "Enregistrement",
        content = {
            Text(
                "Votre compte a été enregistré avec succes, Un email vous a été envoyé pour activer votre compte\nVeuillez vérifier votre boîte mail",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(10.dp)
            )
        },
        onConfirm = {
            nav.navigate(Route.LoginRoute.path) {
                popUpTo(Route.RegisterRoute.path) {
                    inclusive = true
                }
            }
        },
        onDismiss = {
            nav.navigate(Route.LoginRoute.path) {
                popUpTo(Route.RegisterRoute.path) {
                    inclusive = true
                }
            }
        },
        show = alert,
        isSuccess = true
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        RegisterForm(
            additional = {
                Text(
                    "Deja enregistre? Se connecter",
                    modifier = Modifier
                        .clickable {
                            nav.navigate(Route.LoginRoute.path) {
                                popUpTo(Route.RegisterRoute.path) {
                                    inclusive = true
                                }
                            }
                        }
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clip(CircleShape),

                    )
            },
            onError = {
                scope.launch {
                    snackbarHostState.showSnackbar(it)
                }
            },
            onSuccess = {
                alert = true
            },
        )

    }
}