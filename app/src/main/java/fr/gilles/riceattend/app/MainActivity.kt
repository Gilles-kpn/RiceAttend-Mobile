package fr.gilles.riceattend.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.ui.navigation.NavigationContent
import fr.gilles.riceattend.ui.theme.RiceAttendTheme
import fr.gilles.riceattend.ui.viewmodel.ActivityVM
import fr.gilles.riceattend.ui.viewmodel.PaddyFieldVM
import fr.gilles.riceattend.ui.viewmodel.RessourceVM
import fr.gilles.riceattend.ui.viewmodel.WorkerVM
import fr.gilles.riceattend.ui.widget.components.AddPaddyFieldViewModel
import fr.gilles.riceattend.ui.widget.components.AddWorkersViewModel
import fr.gilles.riceattend.ui.widget.components.RiceAttendDialog
import fr.gilles.riceattend.utils.DialogService
import java.lang.ref.WeakReference

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.initSession(WeakReference(this))
        setContent {
            RiceAttendTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val snackbarHostState = remember { SnackbarHostState() }
                        NavigationContent(
                            navHostController = rememberNavController(),
                            snackbarHostState = snackbarHostState
                        )
                        DialogService.dialog?.let { RiceAttendDialog() }
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
        fun ressourceVMFactory() : RessourceVM.Factory
    }

}


