package fr.gilles.riceattend.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.RegisterForm

@Composable
fun RegisterScreen(nav:NavController, snackbarHostState: SnackbarHostState){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RegisterForm(
            additional = {
                Text(
                    "Deja enregistre? Se connecter",
                    modifier = Modifier.clickable {
                        nav.navigate(Route.LoginRoute.path){
                            popUpTo(Route.RegisterRoute.path){
                                inclusive = true
                            }
                        }
                    }.padding(10.dp).fillMaxWidth().clip(CircleShape),

                    )
            }
        )
    }
}