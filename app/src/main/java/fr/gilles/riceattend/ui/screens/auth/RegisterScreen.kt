package fr.gilles.riceattend.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.RegisterForm
import fr.gilles.riceattend.ui.widget.components.OpenDialog
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(nav: NavController, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var alert = remember { mutableStateOf(false) }
        OpenDialog(
            title = "Enregistrement",
            content = {
                Text(
                    "Votre compte a ete enregistre avec succes, vous pouvez maintenant vous connecter",
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
            show = alert.value
        )
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
                alert.value = true
            },
        )
    }
}