package fr.gilles.riceattend.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.ui.widget.LoginFormWidget

@Composable
@Preview
fun LoginScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        LoginFormWidget()
        Spacer(modifier = Modifier.height(13.dp))
        Text("Pas de compte? S'enregister")
        Spacer(modifier = Modifier.height(30.dp))
    }
}