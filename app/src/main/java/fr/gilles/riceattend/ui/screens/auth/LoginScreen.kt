package fr.gilles.riceattend.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.LoginFormWidget
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(nav: NavController, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginFormWidget(
            onSuccess = {
                nav.navigate(Route.MainRoute.path) {
                    popUpTo(Route.LoginRoute.path) {
                        inclusive = true
                    }
                }
            },
            additional = {
                Text(
                    "Pas de compte? S'enregister",
                    modifier = Modifier
                        .clickable {
                            nav.navigate(Route.RegisterRoute.path) {
                                popUpTo(Route.LoginRoute.path) {
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
            }
        )

    }
}