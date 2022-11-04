package fr.gilles.riceattend.app

import android.os.Build
import android.os.Bundle
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
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import fr.gilles.riceattend.R
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.ui.navigation.NavigationContent
import fr.gilles.riceattend.ui.theme.RiceAttendTheme
import fr.gilles.riceattend.ui.viewmodel.ActivityVM
import fr.gilles.riceattend.ui.viewmodel.PaddyFieldVM
import fr.gilles.riceattend.ui.viewmodel.WorkerVM
import fr.gilles.riceattend.ui.widget.components.AddPaddyFieldViewModel
import fr.gilles.riceattend.ui.widget.components.AddWorkersViewModel
import java.lang.ref.WeakReference

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_RiceAttend)
        SessionManager.context = WeakReference(this@MainActivity)
        SessionManager.load()
        setContent {
            RiceAttendTheme {
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

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun paddyFieldVMFactory(): PaddyFieldVM.Factory
        fun activityVMFactory(): ActivityVM.Factory
        fun workerVMFactory():WorkerVM.Factory
        fun addWorkerVMFactory(): AddWorkersViewModel.Factory
        fun addPaddyFieldVMFactory(): AddPaddyFieldViewModel.Factory
    }

}


