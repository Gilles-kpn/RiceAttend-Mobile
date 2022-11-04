package fr.gilles.riceattend.ui.navigation

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import dagger.hilt.android.EntryPointAccessors
import fr.gilles.riceattend.app.MainActivity
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.services.entities.models.ActivityPaddyFieldWithoutActivity
import fr.gilles.riceattend.services.entities.models.ActivityWorkerWithoutActivity
import fr.gilles.riceattend.ui.screens.auth.LoginScreen
import fr.gilles.riceattend.ui.screens.auth.RegisterScreen
import fr.gilles.riceattend.ui.screens.main.MainScreen
import fr.gilles.riceattend.ui.screens.main.details.ActivityPage
import fr.gilles.riceattend.ui.screens.main.details.PaddyFieldPage
import fr.gilles.riceattend.ui.screens.main.details.WorkerPage
import fr.gilles.riceattend.ui.screens.main.lists.*
import fr.gilles.riceattend.ui.viewmodel.ActivityVM
import fr.gilles.riceattend.ui.viewmodel.PaddyFieldVM
import fr.gilles.riceattend.ui.viewmodel.WorkerVM
import fr.gilles.riceattend.ui.widget.components.AddPaddyFieldViewModel
import fr.gilles.riceattend.ui.widget.components.AddWorkersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class Route(
    val path: String,
) {
    object AuthRoute : Route(path = "auth")
    object LoginRoute : Route(path = "login")
    object RegisterRoute : Route(path = "register")
    object MainRoute : Route(path = "main")
    object PaddyFieldsRoute : Route(path = "paddyfields")
    object DashboardRoute : Route(path = "dashboard")
    object ResourcesRoute : Route(path = "resources")
    object SettingRoute : Route(path = "settings")
    object WorkersRoute : Route(path = "workers")
    object WorkerRoute : Route(path = "worker/{code}")
    object PaddyFieldRoute : Route(path = "paddyfield/{code}")
    object ActivitiesRoute : Route(path = "activities")
    object ActivityRoute : Route(path = "activity/{code}")
    object ActivityCreationRoute : Route(path = "activity/create")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationContent(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    NavHost(
        navController = navHostController,
        startDestination = if (SessionManager.session.user == null) Route.AuthRoute.path else Route.MainRoute.path
    ) {
        navigation(startDestination = Route.LoginRoute.path, route = Route.AuthRoute.path) {
            composable(Route.LoginRoute.path) {
                LoginScreen(nav = navHostController, snackbarHostState = snackbarHostState)
            }
            composable(Route.RegisterRoute.path) {
                RegisterScreen(navHostController, snackbarHostState = snackbarHostState)
            }
        }
        navigation(startDestination = Route.DashboardRoute.path, route = Route.MainRoute.path) {

            composable(Route.DashboardRoute.path) {
                DashboardFragment(navHostController)
            }

            composable(Route.ResourcesRoute.path) {
                ResourcesFragment(navHostController)
            }
            composable(Route.WorkersRoute.path) {
                WorkersFragment(navHostController)
            }
            composable(Route.PaddyFieldsRoute.path) {
                PaddyFieldsFragment(navHostController = navHostController)
            }
            composable(Route.SettingRoute.path) {
                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        SettingsFragment(
                            onMenuClick = { scope.launch { scaffoldState.drawerState.open() } }
                        )
                    },
                    scaffoldState = scaffoldState
                )
            }

            composable(Route.ActivityCreationRoute.path) {
                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        ActivityCreationScreen(
                            onMenuClick = { navHostController.popBackStack() },
                            navHostController = navHostController,
                        )
                    },
                    scaffoldState = scaffoldState
                )
            }

            composable(Route.ActivitiesRoute.path) {
                ActivitiesFragment(
                    navHostController = navHostController,
                )
            }

            composable(
                Route.WorkerRoute.path,
                arguments = listOf(navArgument("code") { type = NavType.StringType })
            ) {
                it.arguments?.let { bundle ->
                    WorkerPage(
                        navHostController = navHostController,
                        snackbarHostState = snackbarHostState,
                        viewModel = workerViewModel(bundle["code"] as String),
                    )
                }

            }

            composable(
                Route.ActivityRoute.path,
                arguments = listOf(navArgument("code") { type = NavType.StringType })
            ) {
                it.arguments?.let { bundle ->
                    ActivityPage(
                        onMenuClick = { scope.launch { navHostController.popBackStack() } },
                        navHostController = navHostController,
                        snackbarHostState = snackbarHostState,
                        viewModel = activityViewModel(bundle["code"] as String),
                    )
                }
            }
            composable(
                Route.PaddyFieldRoute.path,
                arguments = listOf(navArgument("code") { type = NavType.StringType })
            ) {
                it.arguments?.let { bundle ->
                    PaddyFieldPage(
                        navHostController = navHostController,
                        snackbarHostState = snackbarHostState,
                        viewModel = paddyFieldViewModel(code = bundle["code"] as String)
                    )

                }

            }

            composable(Route.SettingRoute.path) {
                SettingsFragment(
                    onMenuClick = { scope.launch { scaffoldState.drawerState.open() } }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun paddyFieldViewModel(code: String): PaddyFieldVM {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .paddyFieldVMFactory()
    return viewModel(factory = PaddyFieldVM.provideFactory(factory, code))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun activityViewModel(code: String): ActivityVM {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .activityVMFactory()
    return viewModel(factory = ActivityVM.provideFactory(factory, code))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun workerViewModel(code: String): WorkerVM {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .workerVMFactory()
    return viewModel(factory = WorkerVM.provideFactory(factory, code))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun addWorkerViewModel(
    code: String,
    alreadyExists: List<ActivityWorkerWithoutActivity>,
    onAddWorker: (List<ActivityWorkerWithoutActivity>) -> Unit,
): AddWorkersViewModel {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .addWorkerVMFactory()
    return viewModel(
        factory = AddWorkersViewModel.provideFactory(
            factory,
            alreadyExists = alreadyExists,
            code = code,
            onAddWorker = onAddWorker
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun addPaddyFieldViewModel(
    code: String,
    alreadyExists: List<ActivityPaddyFieldWithoutActivity>,
    onAddPaddyFields: (List<ActivityPaddyFieldWithoutActivity>) -> Unit
): AddPaddyFieldViewModel {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .addPaddyFieldVMFactory()
    return viewModel(
        factory = AddPaddyFieldViewModel.provideFactory(
            factory,
            code,
            alreadyExists,
            onAddPaddyFields
        )
    )
}