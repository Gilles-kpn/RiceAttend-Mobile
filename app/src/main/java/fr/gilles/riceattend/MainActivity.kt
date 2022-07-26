package fr.gilles.riceattend

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.ui.navigation.NavigationContent
import fr.gilles.riceattend.ui.theme.RiceAttendTheme
import java.lang.ref.WeakReference
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.context = WeakReference(this@MainActivity)
        SessionManager.load()
        setContent {
            actionBar?.hide()
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
            RiceAttendTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val snackbarHostState = remember { SnackbarHostState() }
                    NavigationContent(
                        navHostController = rememberNavController(),
                        snackbarHostState = snackbarHostState
                    )
                    Box(Modifier.fillMaxSize(), Alignment.BottomCenter) {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                }
            }
        }
    }

}


