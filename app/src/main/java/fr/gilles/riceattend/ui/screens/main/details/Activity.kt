package fr.gilles.riceattend.ui.screens.main.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.ui.viewmodel.ActivityVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityPage(
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: ActivityVM
) {
    val scope = rememberCoroutineScope()
    when (viewModel.loading) {
        true -> LoadingCard()
        false -> {
            viewModel.activity?.let {
                var dialogText by remember { mutableStateOf<String?>(null) }
                var dialogIsSuccess by remember { mutableStateOf(false) }

                val addWorkersModalBottomSheetState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                val optionsState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AppBar(title = it.name, leftContent = {
                        IconButton(onClick = onMenuClick) {
                            Icon(
                                Icons.Outlined.ArrowBack,
                                "Back ",
                            )
                        }
                    }, rightContent = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    optionsState.show()
                                }
                            },
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                "View More",
                            )
                        }
                    })

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatDate(it.startDate),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(text = formatTime(it.startDate))
                        }
                        Card(
                            modifier = Modifier
                                .padding(20.dp)
                                .size(140.dp)
                                .clip(CircleShape),
                            elevation = 10.dp
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Outlined.Timelapse, "Timelapse")
                                Text(
                                    text = "Durée Totale",
                                    fontSize = 10.sp,
                                )
                                Text(
                                    text = it.duration(),
                                    style = MaterialTheme.typography.h1,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Badge(
                                    backgroundColor = MaterialTheme.colors.primary,
                                    contentColor = MaterialTheme.colors.background,
                                    modifier = Modifier.clip(CircleShape),
                                ) {
                                    Text(
                                        text = it.status.label,
                                        Modifier.padding(
                                            vertical = 5.dp,
                                            horizontal = 10.dp
                                        ),
                                        fontSize = 10.sp,
                                        style = MaterialTheme.typography.body1,
                                    )
                                }
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatDate(it.endDate),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(text = formatTime(it.endDate))
                        }
                    }
                    var selectedIndex by remember { mutableStateOf(0) }
                    val tabs = listOf(
                        "Rizieres",
                        "Ouvriers",
                        "Ressources"
                    )
                    TabRow(
                        selectedTabIndex = selectedIndex,
                        Modifier.padding(horizontal = 10.dp),
                        backgroundColor = MaterialTheme.colors.background,
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = selectedIndex == index,
                                onClick = { selectedIndex = index },
                            ) {
                                Text(
                                    text = tab + if (selectedIndex == index && index == 0) "(${it.activityPaddyFields.size})" else if (selectedIndex == index && index == 1) "(${it.activityWorkers.size})" else if(selectedIndex == index && index == 2)  "(${it.activityResources.size})" else "",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    when (selectedIndex) {
                        0 -> {
                            if (it.activityPaddyFields.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                ) {
                                    items(it.activityPaddyFields.size) { index ->
                                        PaddyFieldTile(
                                            paddyField = it.activityPaddyFields[index].paddyField,
                                            onClick = {

                                            }
                                        )
                                    }
                                }
                            } else {
                                EmptyCard()
                            }
                        }
                        1 -> {
                            if (it.activityWorkers.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                ) {
                                    items(it.activityWorkers.size) { index ->
                                        WorkerTile(
                                            worker = it.activityWorkers[index].worker,
                                            onClick = {
                                            }
                                        )
                                    }
                                }
                            } else {
                                EmptyCard()
                            }
                        }
                        2 -> {
                            if (it.activityResources.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                ) {
                                    items(it.activityResources.size) { index ->
                                        ResourceTile(
                                            resource = it.activityResources[index].resource,
                                            onClick = {

                                            }
                                        )
                                    }
                                }
                            } else {
                                EmptyCard()
                            }
                        }
                    }
                }

                ActivityOptions(state = optionsState, viewModel = viewModel)

            }
        }
    }

}

