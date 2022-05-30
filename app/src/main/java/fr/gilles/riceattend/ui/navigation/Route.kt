package fr.gilles.riceattend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import fr.gilles.riceattend.ui.screens.auth.LoginScreen
import fr.gilles.riceattend.ui.screens.auth.RegisterScreen

sealed class Route(
    val path:String,
) {
    object AuthRoute: Route(path = "auth")
    object LoginRoute: Route(path = "login")
    object  RegisterRoute: Route(path = "register")
    object MainRoute: Route(path = "main")
}

@Composable
fun NavigationContent(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = Route.AuthRoute.path ){
        navigation(startDestination = Route.LoginRoute.path, route = Route.AuthRoute.path){
            composable(Route.LoginRoute.path){
                LoginScreen(nav = navHostController)
            }
            composable(Route.RegisterRoute.path){
                RegisterScreen(navHostController)
            }
        }
    }
}