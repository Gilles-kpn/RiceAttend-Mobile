package fr.gilles.riceattend.ui.screens.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.components.userGravatar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    nav: NavController,
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
) {
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Drawer(nav, scope, scaffoldState)
        },
        drawerGesturesEnabled = true,
        content = {  content() },
        floatingActionButton = floatingActionButton
    )
}

@Composable
fun Drawer(
    nav: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.primary,
                            )
                        )
                    ),
                horizontalAlignment = Alignment.Start
            ) {
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(20.dp)
                        .clip(CircleShape)
                ) {
                    AsyncImage(
                        model = userGravatar(),
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                SessionManager.session.user?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = it.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 20.dp),
                                color = Color.White
                            )
                            Text(
                                text = it.email,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 10.dp, start = 20.dp),
                                color = Color.White
                            )
                        }
                        IconButton(onClick = {
                            scope.launch { scaffoldState.drawerState.close() }
                            nav.navigate(Route.AccountRoute.path)
                        }) {
                            Icon(Icons.Outlined.Edit, "Edit", tint = Color.White)
                        }
                    }
                }
                if(SessionManager.session.user == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Guest",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 20.dp),
                                color = Color.White
                            )
                            Text(
                                text = "Offline mode",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 10.dp, start = 20.dp),
                                color = Color.White
                            )
                        }
                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Outlined.WifiOff, "No network", tint = Color.White)
                        }
                    }
                }
            }
            val pages = remember {
                mutableListOf(
                    mapOf(
                        "route" to Route.DashboardRoute.path,
                        "title" to "Tableau de bord",
                        "icon" to Icons.Outlined.Dashboard
                    ),
                    mapOf(
                        "route" to Route.ActivitiesRoute.path,
                        "title" to "Activités",
                        "icon" to Icons.Outlined.Event
                    ),
                    mapOf(
                        "route" to Route.PaddyFieldsRoute.path,
                        "title" to "Rizières",
                        "icon" to Icons.Outlined.Landscape
                    ),
                    mapOf(
                        "route" to Route.ResourcesRoute.path,
                        "title" to "Ressources",
                        "icon" to Icons.Outlined.Build
                    ),
                    mapOf(
                        "route" to Route.WorkersRoute.path,
                        "title" to "Ouvriers",
                        "icon" to Icons.Outlined.People
                    ),
                )
            }
            pages.forEach {
                val backgroundColor: Color =
                    if (it["route"] == nav.currentDestination?.route) MaterialTheme.colors.primary else Color.Transparent
                val frontColor: Color =
                    if (it["route"] == nav.currentDestination?.route) MaterialTheme.colors.background else Color.White
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable {

                            scope.launch { scaffoldState.drawerState.close() }
                            nav.navigate(it["route"] as String)

                        }
                        .background(backgroundColor),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (it["route"] == nav.currentDestination?.route) {
                        Icon(
                            it["icon"] as ImageVector,
                            "Dashboard icons",
                            modifier = Modifier.padding(
                                start = 12.dp,
                                top = 8.dp,
                                end = 8.dp,
                                bottom = 8.dp
                            ),
                            tint = frontColor
                        )
                        Text(text = it["title"].toString(), color = frontColor)
                    } else {
                        Icon(
                            it["icon"] as ImageVector,
                            "Dashboard icons",
                            modifier = Modifier.padding(
                                start = 12.dp,
                                top = 8.dp,
                                end = 8.dp,
                                bottom = 8.dp
                            ),
                        )
                        Text(text = it["title"].toString())
                    }

                }
            }

        }


        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Action", modifier = Modifier.padding(start = 10.dp))
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        scope.launch { scaffoldState.drawerState.close() }
                        nav.navigate(
                            Route.SettingRoute.path
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Settings,
                    "Resources icons",
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    )
                )
                Text(text = "Parametres")
            }
            SessionManager.session.user?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            SessionManager.clear()
                            nav.navigate(Route.LoginRoute.path) {
                                popUpTo(Route.MainRoute.path) {
                                    inclusive = true
                                }
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Logout,
                        "Resources icons",
                        modifier = Modifier.padding(
                            start = 12.dp,
                            top = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    Text(text = "Deconnexion")
                }
            }

        }
    }
}


