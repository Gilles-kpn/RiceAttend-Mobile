package fr.gilles.riceattend.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.LoginFormWidget

@Composable
fun LoginScreen(nav:NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginFormWidget()
        Spacer(modifier = Modifier.height(13.dp))
        Text("Pas de compte? S'enregister",
            modifier = Modifier.clickable {
                nav.navigate(Route.RegisterRoute.path)
            }.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}