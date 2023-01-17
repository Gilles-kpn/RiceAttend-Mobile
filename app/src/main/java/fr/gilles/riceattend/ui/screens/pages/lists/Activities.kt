package fr.gilles.riceattend.ui.screens.pages.lists

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.misc.OnBottomReached
import fr.gilles.riceattend.models.ActivityStatusList
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.pages.Drawer
import fr.gilles.riceattend.ui.viewmodel.ActivitiesVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivitiesFragment(
    viewModel: ActivitiesVM = hiltViewModel(),
    navHostController: NavController = rememberNavController(),
) {
    val scaffoldState = rememberScaffoldState()
    val filterState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(navHostController, scope, scaffoldState) },
        topBar = {
            AppBar(title = "Activités", leftContent = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Outlined.Menu, "Menu")
                }
            }, rightContent = {
                IconButton(onClick = {
                    scope.launch {
                        filterState.show()
                    }
                }) {
                    Icon(Icons.Outlined.FilterList, "Add")
                }
            })
        },
        drawerGesturesEnabled = true,
        content = { padding ->
            when (viewModel.loading) {
                true -> {
                    LoadingCard()

                }
                false ->
                    viewModel.activities?.let {
                        if (it.empty == true && it.first == true) {
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Aucune activité")
                            }
                        } else {
                            val listState = rememberLazyListState()
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                state = listState,
                            ) {
                                it.forEach { activity ->
                                    item {
                                        Box(Modifier.padding(horizontal = 10.dp)) {
                                            ActivityTile(onClick = {
                                                navHostController.navigate(
                                                    Route.ActivityRoute.path.replace(
                                                        "{code}",
                                                        activity.code
                                                    )
                                                ) {
                                                }
                                            }, activity = activity) {
                                            }
                                        }
                                    }
                                }

                                if (viewModel.loadingMore) {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { viewModel.viewMore() }
                                                .padding(vertical = 7.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }

                                    }
                                }
                            }
                            listState.OnBottomReached {
                                viewModel.viewMore()
                            }

                        }
                    } ?: run {
                        EmptyCard()
                    }

            }
        },


        floatingActionButton = {
            FloatingActionButton(onClick = {
                navHostController.navigate(Route.ActivityCreationRoute.path)
            }) {
                Icon(Icons.Outlined.Add, "Add")
            }
        }
    )
    ModalBottomSheetLayout(
        sheetState = filterState,
        sheetContent = {
            Text("Filtrer", fontWeight = FontWeight.Bold, modifier = Modifier.padding(15.dp))
            InputWidget(state = viewModel.name, title = "Filtrer par nom")
            Text(text = "Filtrer par statut", modifier = Modifier.padding(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp, 15.dp)
            ) {
                ActivityStatusList.forEach { status ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    )
                    {
                        Text(text = status.label)
                        Checkbox(
                            checked = viewModel.params.status.contains(status),
                            onCheckedChange = { value ->
                                if (value) {
                                   viewModel.params = viewModel.params.copy(status = viewModel.params.status.plus(status))
                                } else {
                                    viewModel.params = viewModel.params.copy(status = viewModel.params.status.minus(status))
                                }
                            })
                        }
                }
            }

            Button(
                onClick = {
                    viewModel.filter()
                    scope.launch {
                        filterState.hide()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(50.dp)
            ) {
                Text("Filtrer", style = MaterialTheme.typography.button)
            }
        }) {

    }

}


