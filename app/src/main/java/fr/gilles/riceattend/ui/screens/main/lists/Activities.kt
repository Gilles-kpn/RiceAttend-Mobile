package fr.gilles.riceattend.ui.screens.main.lists

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.main.Drawer
import fr.gilles.riceattend.ui.viewmodel.ActivitiesVM
import fr.gilles.riceattend.ui.widget.components.ActivityTile
import fr.gilles.riceattend.ui.widget.components.AppBar
import fr.gilles.riceattend.ui.widget.components.LoadingCard
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivitiesFragment(
    viewModel: ActivitiesVM = hiltViewModel(),
    navHostController: NavController = rememberNavController(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(navHostController, scope, scaffoldState) },
        topBar = {
            AppBar(title = "Activites", leftContent = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Outlined.Menu, "Menu",)
                }
            }, rightContent = {
                IconButton(onClick = {  }) {
                    Icon(Icons.Outlined.FilterList, "Add",)
                }
            })
        },
        drawerGesturesEnabled = true,
        content = { padding->
            when(viewModel.loading) {
                true ->{
                    LoadingCard()
                }
                false ->{
                    viewModel.activities.let {
                        if (it.isEmpty()) {
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
                                modifier = Modifier.fillMaxSize(),
                                state = listState,
                            ) {
                                viewModel.activities.forEach { activity ->
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navHostController.navigate(Route.ActivityCreationRoute.path)
            }) {
                Icon(Icons.Outlined.Add, "Add")
            }
        }
    )

}


