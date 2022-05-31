package fr.gilles.riceattend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.ui.navigation.NavigationContent
import fr.gilles.riceattend.ui.theme.RiceAttendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RiceAttendTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    NavigationContent(navHostController = rememberNavController(), snackbarHostState = snackbarHostState)
                    Box(Modifier.fillMaxSize(), Alignment.BottomCenter) {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                }
            }
        }
    }
}
