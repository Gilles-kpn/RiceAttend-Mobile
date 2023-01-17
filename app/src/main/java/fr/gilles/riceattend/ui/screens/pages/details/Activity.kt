package fr.gilles.riceattend.ui.screens.pages.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.models.ActivityStatus
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.viewmodel.ActivityVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityPage(
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController,
    viewModel: ActivityVM
) {
    val scope = rememberCoroutineScope()
    when (viewModel.loading) {
        true -> LoadingCard()
        false -> {
            viewModel.activity?.let {
                var existBottomSheet by remember { mutableStateOf(false) }
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
                                existBottomSheet = true
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
                    var toggleViewMode by remember { mutableStateOf(false) }
                    Card(
                        Modifier
                            .clickable {
                                toggleViewMode = !toggleViewMode
                            }
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)) {
                        Row(
                            Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Vue")
                            Icon(Icons.Outlined.Shuffle, "Shuffle")
                        }
                    }
                    when(toggleViewMode){
                        true ->{
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                elevation = 4.dp
                            ){
                                Column(modifier = Modifier.fillMaxWidth()){
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Outlined.TextFields, "name", modifier = Modifier.padding(end = 10.dp))
                                            Text("Nom de l'activité")
                                        }
                                        Text(it.name, fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Outlined.CalendarToday, "Calendar", modifier = Modifier.padding(end = 10.dp))
                                            Text("Date de début")
                                        }
                                        Text(formatDate(it.startDate), fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Outlined.CalendarToday, "Calendar", modifier = Modifier.padding(end = 10.dp))
                                            Text("Date de fin")
                                        }
                                        Text(formatDate(it.endDate), fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Outlined.Tag, "Tag", modifier = Modifier.padding(end = 10.dp))
                                            Text("Type d'activité")
                                        }
                                        Text(it.type?.label ?: "Non défini", fontWeight = FontWeight.Bold)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Outlined.Money, "Money", modifier = Modifier.padding(end = 10.dp))
                                            Text("Cout estimé")
                                        }
                                        Text(it.cost().toString() + " FCFA", fontWeight = FontWeight.Bold)
                                    }
                                    Text("Description", fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
                                    Text(it.description, modifier = Modifier.padding(10.dp))
                                }
                            }
                        }
                        false ->{
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
                                    Text("Du")
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
                                            backgroundColor = when (it.status) {
                                                ActivityStatus.INIT -> Color.LightGray
                                                ActivityStatus.CANCELLED, ActivityStatus.UNDONE -> MaterialTheme.colors.error
                                                ActivityStatus.IN_PROGRESS -> Color(0xFFFF5722)
                                                ActivityStatus.DONE -> MaterialTheme.colors.primary
                                            },
                                            contentColor = when (it.status) {
                                                ActivityStatus.INIT -> Color.Black
                                                ActivityStatus.CANCELLED, ActivityStatus.UNDONE, ActivityStatus.DONE, ActivityStatus.IN_PROGRESS  -> Color.White
                                            },
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
                                    Text("Au")
                                    Text(
                                        text = formatDate(it.endDate),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(text = formatTime(it.endDate))
                                }
                            }
                            Text(
                                text = "Coût estimé: ${it.cost()} FCFA",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth(),
                                fontWeight = FontWeight.Bold
                            )
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
                                    text = tab + if (selectedIndex == index && index == 0) "(${it.activityPaddyFields.size})" else if (selectedIndex == index && index == 1) "(${it.activityWorkers.size})" else if (selectedIndex == index && index == 2) "(${it.activityResources.size})" else "",
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
                                        .fillMaxSize()
                                        .padding(10.dp),
                                ) {
                                    items(it.activityPaddyFields.size) { index ->
                                        Box(
                                            Modifier.fillMaxWidth()
                                        ) {
                                            var displayActivityPaddyFieldOptions by remember {
                                                mutableStateOf(
                                                    false
                                                )
                                            }
                                            PaddyFieldTile(
                                                paddyField = it.activityPaddyFields[index].paddyField,
                                                onClick = {
                                                    navHostController.navigate(
                                                        Route.PaddyFieldRoute.path.replace(
                                                            "{code}",
                                                            it.activityPaddyFields[index].paddyField.code
                                                        )
                                                    )
                                                },
                                                onLongClick = {
                                                    if(it.status != ActivityStatus.DONE && it.status != ActivityStatus.CANCELLED)
                                                     displayActivityPaddyFieldOptions = true
                                                },
                                                badgeContent = {
                                                    Box(
                                                        Modifier
                                                            .size(15.dp)
                                                            .clip(CircleShape)
                                                            .background(
                                                                color = when (it.activityPaddyFields[index].status) {
                                                                    ActivityStatus.INIT -> Color.LightGray
                                                                    ActivityStatus.CANCELLED, ActivityStatus.UNDONE -> MaterialTheme.colors.error
                                                                    ActivityStatus.IN_PROGRESS -> Color(
                                                                        0xFFFF5722
                                                                    )
                                                                    ActivityStatus.DONE -> MaterialTheme.colors.primary
                                                                }
                                                            )
                                                    )
                                                },
                                            )
                                            if (it.activityPaddyFields[index].status != ActivityStatus.DONE && it.activityPaddyFields[index].status != ActivityStatus.CANCELLED) {
                                                DropdownMenu(
                                                    expanded = displayActivityPaddyFieldOptions,
                                                    onDismissRequest = {
                                                        displayActivityPaddyFieldOptions = false
                                                    }
                                                ) {
                                                    if (it.activityPaddyFields[index].status == ActivityStatus.INIT)
                                                        DropdownMenuItem(onClick = {
                                                            displayActivityPaddyFieldOptions = false
                                                            viewModel.markAsStartedOnPaddyField(
                                                                it.activityPaddyFields[index].paddyField
                                                            )
                                                        }) {
                                                            Row(
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    Icons.Outlined.PlayArrow,
                                                                    "start",
                                                                    Modifier.padding(end = 10.dp)
                                                                )
                                                                Text(text = "Marquer comme commencé")
                                                            }
                                                        }
                                                    if (it.activityPaddyFields[index].status == ActivityStatus.IN_PROGRESS) {
                                                        DropdownMenuItem(onClick = {
                                                            displayActivityPaddyFieldOptions = false
                                                            viewModel.markAsDoneOnPaddyField(
                                                                it.activityPaddyFields[index].paddyField
                                                            )
                                                        }) {
                                                            Row(
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    Icons.Outlined.Check,
                                                                    "done",
                                                                    Modifier.padding(end = 10.dp)
                                                                )
                                                                Text(text = "Marquer comme terminé")
                                                            }
                                                        }
                                                        DropdownMenuItem(onClick = {
                                                            displayActivityPaddyFieldOptions = false
                                                            viewModel.cancelActivityOnPaddyField(
                                                                it.activityPaddyFields[index].paddyField
                                                            )
                                                        }) {
                                                            Row(
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    Icons.Outlined.Cancel,
                                                                    "cancel",
                                                                    Modifier.padding(end = 10.dp)
                                                                )
                                                                Text(text = "Marquer comme annulé")
                                                            }
                                                        }
                                                    }
                                                    if (it.activityPaddyFields.size > 1)
                                                        DropdownMenuItem(onClick = {
                                                            displayActivityPaddyFieldOptions = false
                                                            viewModel.removeActivityPaddyFieldFromActivity(
                                                                it.activityPaddyFields[index].paddyField
                                                            )
                                                        }) {
                                                            Row(
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    Icons.Outlined.Delete,
                                                                    "Delete Worker",
                                                                    Modifier.padding(end = 10.dp)
                                                                )
                                                                Text(text = "Retirer de l'activité")
                                                            }
                                                        }
                                                }
                                            }
                                        }
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
                                        Box(Modifier.fillMaxWidth()) {
                                            var displayWorkerOptions by remember {
                                                mutableStateOf(
                                                    false
                                                )
                                            }
                                            WorkerTile(
                                                worker = it.activityWorkers[index].worker,
                                                onClick = {
                                                    navHostController.navigate(
                                                        Route.WorkerRoute.path.replace(
                                                            "{code}",
                                                            it.activityWorkers[index].worker.code
                                                        )
                                                    )
                                                },
                                                onLongClick = {
                                                    displayWorkerOptions = true
                                                },
                                            ) {
                                                Text(
                                                    text = "Revenu: ${it.activityWorkers[index].price} FCFA",
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                            if(it.status != ActivityStatus.DONE && it.status != ActivityStatus.CANCELLED)
                                            DropdownMenu(
                                                expanded = displayWorkerOptions,
                                                onDismissRequest = {
                                                    displayWorkerOptions = false
                                                }) {
                                                DropdownMenuItem(onClick = {
                                                    displayWorkerOptions = false
                                                    viewModel.removeWorkerFromActivity(
                                                        it.activityWorkers[index].worker
                                                    )
                                                }) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            Icons.Outlined.Delete,
                                                            "Delete Worker",
                                                            Modifier.padding(end = 10.dp)
                                                        )
                                                        Text(text = "Supprimer")
                                                    }
                                                }
                                            }
                                        }

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
                                        var displayResourceOptions by remember {
                                            mutableStateOf(
                                                false
                                            )
                                        }
                                        Box(Modifier.fillMaxWidth()) {
                                            ResourceTile(
                                                resource = it.activityResources[index].resource,
                                                onClick = {
                                                    navHostController.navigate(
                                                        Route.ResourceRoute.path.replace(
                                                            "{code}",
                                                            it.activityResources[index].resource.code
                                                        )
                                                    )
                                                },
                                                onLongClick = {
                                                    displayResourceOptions = true
                                                },
                                            ) {
                                                Column {
                                                    Text("Quantité utilisée: ${it.activityResources[index].quantity.toInt()}")
                                                    Text(
                                                        "Prix: ${it.activityResources[index].value} FCFA",
                                                        fontWeight = FontWeight.SemiBold,
                                                        modifier = Modifier.padding(top = 3.dp)
                                                    )
                                                }
                                            }
                                            if(it.status != ActivityStatus.DONE && it.status != ActivityStatus.CANCELLED)
                                            DropdownMenu(
                                                expanded = displayResourceOptions,
                                                onDismissRequest = {
                                                    displayResourceOptions = false
                                                }
                                            ) {
                                                DropdownMenuItem(onClick = {
                                                    displayResourceOptions = false
                                                    viewModel.modifyResourceQuantityOnActivity(
                                                        it.activityResources[index].resource
                                                    )
                                                }) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            Icons.Outlined.Edit,
                                                            "Edit",
                                                            Modifier.padding(end = 10.dp)
                                                        )
                                                        Text(text = "Modifier la quantité")
                                                    }
                                                }
                                                DropdownMenuItem(onClick = {
                                                    displayResourceOptions = false
                                                    viewModel.removeResourceFromActivity(
                                                        it.activityResources[index].resource
                                                    )
                                                }) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            Icons.Outlined.Delete,
                                                            "Delete resource",
                                                            Modifier.padding(end = 10.dp)
                                                        )
                                                        Text(text = "Retirer de l'activité")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                EmptyCard()
                            }
                        }
                    }
                }

                if (existBottomSheet)
                    ActivityOptions(
                        state = optionsState,
                        viewModel = viewModel,
                        onAddWorker = { added ->
                            viewModel.activity?.let {
                                viewModel.activity =
                                    it.copy(activityWorkers = it.activityWorkers.plus(added))
                            }
                            existBottomSheet = false
                        },
                        onAddPaddyField = { added ->
                            viewModel.activity?.let {
                                viewModel.activity =
                                    it.copy(activityPaddyFields = it.activityPaddyFields.plus(added))
                            }
                            existBottomSheet = false

                        },
                        onAddResource = { added ->
                            viewModel.activity?.let {
                                viewModel.activity = it.copy(
                                    activityResources = it.activityResources.plus(added)
                                )
                            }
                            existBottomSheet = false

                        },
                        onActivityPlainly = {
                            viewModel.plainlyActivityOnAgenda()
                            scope.launch {
                                optionsState.hide()
                            }
                            existBottomSheet = false

                        },
                        onActivityStarted = {
                            viewModel.markAsStarted()
                            scope.launch {
                                optionsState.hide()
                            }
                            existBottomSheet = false

                        },
                        onActivityDone = {
                            viewModel.markAsDone()
                            scope.launch {
                                optionsState.hide()
                            }
                            existBottomSheet = false

                        },
                        onActivityCancelled = {
                            viewModel.cancelActivity()
                            scope.launch {
                                optionsState.hide()
                            }
                            existBottomSheet = false

                        },
                        onActivityDeleted = {
                            viewModel.deleteActivity(
                                onSuccess = {
                                    navHostController.navigate(Route.ActivitiesRoute.path) {
                                        popUpTo(Route.MainRoute.path) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    )

            }
        }
    }

}

