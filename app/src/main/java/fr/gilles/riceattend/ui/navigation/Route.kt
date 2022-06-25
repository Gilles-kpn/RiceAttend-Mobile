package fr.gilles.riceattend.ui.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.ui.screens.auth.LoginScreen
import fr.gilles.riceattend.ui.screens.auth.RegisterScreen
import fr.gilles.riceattend.ui.screens.main.MainScreen
import fr.gilles.riceattend.ui.screens.main.fragments.*
import fr.gilles.riceattend.ui.screens.main.modelstemplate.PaddyFieldModelScreen
import fr.gilles.riceattend.ui.screens.main.modelstemplate.PaddyFieldViewModel
import fr.gilles.riceattend.ui.screens.main.modelstemplate.WorkerModelScreen
import fr.gilles.riceattend.ui.screens.main.modelstemplate.WorkerViewModel
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
    object ActivityCreationRoute:Route(path = "activity/create")
}

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
                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        DashboardFragment(
                            onMenuClick = { scope.launch { scaffoldState.drawerState.open() } }
                        )
                    },
                    scaffoldState = scaffoldState
                )
            }
            composable(Route.ResourcesRoute.path) {
                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        ResourcesFragment(
                            onMenuClick = { scope.launch { scaffoldState.drawerState.open() } }
                        )
                    },
                    scaffoldState = scaffoldState
                )
            }
            composable(Route.WorkersRoute.path) {
                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        WorkersFragment(
                            onMenuClick = { scope.launch { scaffoldState.drawerState.open() } },
                            snackbarHostState = snackbarHostState,
                            navHostController = navHostController
                        )
                    },
                    scaffoldState = scaffoldState
                )
            }
            composable(Route.PaddyFieldsRoute.path) {

                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        PaddyFieldsFragment(
                            onMenuClick = { scope.launch { scaffoldState.drawerState.open() } },
                            snackBarHostState = snackbarHostState,
                            navHostController = navHostController,
                        )
                    },
                    scaffoldState = scaffoldState,
                )
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

            composable(Route.ActivityCreationRoute.path){
                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        ActivityCreationScreen()
                    },
                    scaffoldState = scaffoldState
                )
            }

            composable(Route.ActivitiesRoute.path) {
                MainScreen(
                    nav = navHostController,
                    snackbarHostState = snackbarHostState,
                    content = {
                        ActivitiesFragment(
                            onMenuClick = { scope.launch { scaffoldState.drawerState.open() } },
                            snackbarHostState= snackbarHostState,
                            navHostController = navHostController,
                        )
                    },
                    scaffoldState = scaffoldState
                )
            }
            //path = Route.WorkerRoute.path with workerId String
            composable(
                Route.WorkerRoute.path,
                arguments = listOf(navArgument("code") { type = NavType.StringType })
            ) {
                it.arguments?.let { bundle ->
                    WorkerModelScreen(
                        navHostController = navHostController,
                        snackbarHostState = snackbarHostState,
                        viewModel = WorkerViewModel(bundle["code"] as String),
                    )
                }

            }
            composable(
                Route.PaddyFieldRoute.path,
                arguments = listOf(navArgument("code") { type = NavType.StringType })
            ) {
                it.arguments?.let { bundle ->
                    PaddyFieldModelScreen(
                        navHostController = navHostController,
                        snackbarHostState = snackbarHostState,
                        viewModel = PaddyFieldViewModel(bundle["code"] as String),
                    )
                }

            }
        }
    }
}