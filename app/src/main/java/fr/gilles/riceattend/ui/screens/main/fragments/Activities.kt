package fr.gilles.riceattend.ui.screens.main.fragments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.viewmodel.ActivitiesVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivitiesFragment(
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController,
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState,
    viewModel: ActivitiesVM = remember { ActivitiesVM() }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBar(title = "Activites", leftContent = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.Menu, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = { navHostController.navigate(Route.ActivityCreationRoute.path) }) {
                Icon(Icons.Outlined.Add, "Add",tint = MaterialTheme.colors.background )
            }
        })
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                InputWidget(
                    state = viewModel.searchState,
                    title = "Rechercher",
                    trailing = {
                        IconButton(onClick = {
                        }) {
                            Icon(Icons.Outlined.Search, "Rechercher")
                        }
                    },
                    roundedCornerShape = RoundedCornerShape(30.dp)
                )
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.FilterList, "Filtrer")
                        Text("Filtrer", modifier = Modifier.padding(10.dp))
                    }
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())
                    ){
                        listOf(
                            mapOf("icon" to Icons.Outlined.TimerOff, "status" to ActivityStatus.INIT ),
                            mapOf("icon" to Icons.Outlined.DoneAll, "status" to ActivityStatus.DONE),
                            mapOf("icon" to Icons.Outlined.Timer, "status" to ActivityStatus.IN_PROGRESS ),
                            mapOf("icon" to Icons.Outlined.Snooze, "status" to ActivityStatus.UNDONE ),
                            mapOf("icon" to Icons.Outlined.Cancel, "status" to ActivityStatus.CANCELLED ),
                        ).forEach {
                            var alreadyInFilterParams by remember {
                                mutableStateOf(viewModel.params.status.contains((it["status"] as ActivityStatus).value))
                            }
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .clickable {
                                        if (alreadyInFilterParams) {
                                            viewModel.params.status -= (it["status"] as ActivityStatus).value
                                            alreadyInFilterParams = false
                                        } else {
                                            viewModel.params.status += (it["status"] as ActivityStatus).value
                                            alreadyInFilterParams = true
                                        }
                                    }
                                    .clip(
                                        CircleShape
                                    )
                                    .background(if (alreadyInFilterParams) MaterialTheme.colors.primary else MaterialTheme.colors.background)
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(it["icon"] as  ImageVector, "Filtrer", tint = if (!alreadyInFilterParams) MaterialTheme.colors.primary else MaterialTheme.colors.background)
                                Text((it["status"] as ActivityStatus).label, modifier = Modifier.padding(10.dp), color = if (!alreadyInFilterParams) MaterialTheme.colors.primary else MaterialTheme.colors.background)
                            }
                        }

                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            when (viewModel.loading) {
                true -> {
                    LoadingCard()
                }
                false -> {
                    viewModel.activities.let {
                        if (it.isEmpty()) {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Aucune activité")
                            }
                        } else {
                            val listState = rememberLazyListState()
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = listState,
                            ) {
                                items(viewModel.activities.size){index->
                                    ActivityTile(activity = viewModel.activities[index], onClick = {
                                        navHostController.navigate(
                                            Route.ActivityRoute.path.replace(
                                                "{code}",
                                                viewModel.activities[index].code
                                            )
                                        ){
                                        }
                                    })
                                }

                                if(viewModel.hasMore)
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.viewMore() }
                                            .padding(vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ){
                                        if(!viewModel.loadingMore)
                                            Text(text = "Cliquer ici pour voir plus")
                                        else CircularProgressIndicator()
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}


