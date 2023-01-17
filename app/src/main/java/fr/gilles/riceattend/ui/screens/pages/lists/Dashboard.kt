package fr.gilles.riceattend.ui.screens.pages.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.pages.Drawer
import fr.gilles.riceattend.ui.viewmodel.DashboardViewModel
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@Preview("LineChart")
fun DashboardFragment(
    navHostController: NavController = rememberNavController(),
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(navHostController, scope, scaffoldState) },
        topBar = {
            AppBar(
                title = "Tableau de bord",
                leftContent = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Outlined.Menu, "Menu",)
                    }
                })
        },
        drawerGesturesEnabled = true,
        content = { padding ->
            when (viewModel.loading) {
                true -> LoadingCard()
                false -> viewModel.statistics?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            "Mes Statistiques",
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 15.dp)
                        )
                        Column(Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth()) {
                                Card(
                                    Modifier
                                        .padding(10.dp)
                                        .weight(1f).clickable {
                                            navHostController.navigate(Route.ActivitiesRoute.path)
                                        }

                                ) {
                                    Column(
                                        Modifier.fillMaxWidth().background(Color(0x40E91E63))
                                            .padding(horizontal = 10.dp, vertical = 20.dp)
                                            ,
                                        horizontalAlignment = Alignment.End

                                    ) {
                                        Icon(
                                            Icons.Outlined.Event,
                                            contentDescription = "card",
                                            tint = Color.Black,
                                            modifier = Modifier.
                                            padding(vertical = 20.dp, horizontal = 10.dp).clip(RoundedCornerShape(40.dp)).background(
                                                Color.White).padding(10.dp)
                                        )

                                        Row(
                                            Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = "Activités",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row(
                                            Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(text = it.activitiesNumber.toString(), fontSize = 14.sp)
                                        }
                                    }
                                }

                                Card(
                                    Modifier
                                        .padding(10.dp)
                                        .weight(1f).clickable {
                                            navHostController.navigate(Route.PaddyFieldsRoute.path)
                                        }

                                ) {
                                    Column(
                                        Modifier.fillMaxWidth().background(Color(0x40673AB7)).padding(horizontal = 10.dp, vertical = 20.dp),
                                        horizontalAlignment = Alignment.End

                                    ) {
                                        Icon(
                                            Icons.Outlined.Landscape,
                                            contentDescription = "card",
                                            tint = Color.Black,
                                            modifier = Modifier.
                                            padding(vertical = 20.dp, horizontal = 10.dp).clip(RoundedCornerShape(40.dp)).background(
                                                Color.White).padding(10.dp)
                                        )

                                        Row(
                                            Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = "Rizieres",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row(
                                            Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(text = it.paddyFieldsNumber.toString(), fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                            Row(Modifier.fillMaxWidth()) {
                                Card(
                                    Modifier
                                        .padding(10.dp)
                                        .weight(1f).clickable {
                                            navHostController.navigate(Route.WorkersRoute.path)
                                        }

                                ) {
                                    Column(
                                        Modifier.fillMaxWidth().background(Color(0x404CAF50)).padding(horizontal = 10.dp, vertical = 20.dp),
                                        horizontalAlignment = Alignment.End

                                    ) {
                                        Icon(
                                            Icons.Outlined.People,
                                            contentDescription = "card",
                                            tint = Color.Black,
                                            modifier = Modifier.
                                            padding(vertical = 20.dp, horizontal = 10.dp).clip(RoundedCornerShape(40.dp)).background(
                                                Color.White).padding(10.dp)
                                        )

                                        Row(
                                            Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = "Ouvriers",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row(
                                            Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(text = it.workersNumber.toString(), fontSize = 14.sp)
                                        }
                                    }
                                }

                                Card(
                                    Modifier
                                        .padding(10.dp)
                                        .weight(1f).clickable {
                                            navHostController.navigate(Route.ResourcesRoute.path)
                                        }

                                ) {
                                    Column(
                                        Modifier.fillMaxWidth().background(Color(0x33FF5722)).padding(horizontal = 10.dp, vertical = 20.dp),
                                        horizontalAlignment = Alignment.End

                                    ) {
                                        Icon(
                                            Icons.Outlined.Build,
                                            contentDescription = "card",
                                            tint = Color.Black,
                                            modifier = Modifier.
                                            padding(vertical = 20.dp, horizontal = 10.dp).clip(RoundedCornerShape(40.dp)).background(
                                                Color.White).padding(10.dp)
                                        )

                                        Row(
                                            Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = "Ressources",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row(
                                            Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(text = it.resourcesNumber.toString(), fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                        Text(
                            "Mes Analyses",
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 15.dp)
                        )
                        LineChartView(it.activitiesData)
                        ActivityByStatus(it.activitiesData)
                    }
                } ?: run {
                    EmptyCard()
                }
            }
        }
    )


}

