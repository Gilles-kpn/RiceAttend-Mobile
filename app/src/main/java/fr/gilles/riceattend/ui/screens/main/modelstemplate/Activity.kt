package fr.gilles.riceattend.ui.screens.main.modelstemplate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
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
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.viewmodel.ActivityVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch
import java.time.Duration

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
                var dialogText by remember { mutableStateOf<String?>(null) };
                var dialogIsSuccess by remember { mutableStateOf(false) }
                val addPaddyFieldModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                val addWorkersModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                if (dialogText != null) {
                    OpenDialog(
                        content = { dialogText?.let { Text(text = it) } },
                        title = "Attention",
                        onDismiss = { dialogText = null },
                        onConfirm = { dialogText = null },
                        show = dialogText != null,
                        isSuccess = dialogIsSuccess
                    )
                }

                Column(modifier = Modifier
                    .fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.primary)
                            .padding(bottom = 15.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = onMenuClick) {
                                    Icon(
                                        Icons.Outlined.ArrowBack,
                                        "Back ",
                                        tint = MaterialTheme.colors.background
                                    )
                                }
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.h1,
                                    color = MaterialTheme.colors.background,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
                            Box {
                                var expanded by remember { mutableStateOf(false) }


                                IconButton(
                                    onClick = { expanded = true },
                                    modifier = Modifier.padding(end = 12.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.MoreVert,
                                        "View More",
                                        tint = MaterialTheme.colors.background
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    modifier = Modifier.width(200.dp),
                                    onDismissRequest = { expanded = false },
                                ) {
                                    DropdownMenuItem(
                                        onClick = {
                                            viewModel.plainlyActivityOnAgenda(
                                                onSuccess = {
                                                    dialogText =
                                                        "Activité ajoutée à l'agenda"
                                                    dialogIsSuccess = true
                                                },
                                                onError = {
                                                    dialogText =
                                                        "Erreur lors de l'ajout à l'agenda"
                                                    dialogIsSuccess = false
                                                }
                                            )
                                        },
                                    ) {
                                        Icon(Icons.Outlined.CalendarToday, "planification")
                                        Text(
                                            "Planifier l'activité",
                                            modifier = Modifier.padding(start = 5.dp)
                                        )
                                    }
                                    if (it.status == ActivityStatus.INIT) {
                                        DropdownMenuItem(
                                            onClick = {},
                                        ) {
                                            Icon(Icons.Outlined.Edit, "Edit")
                                            Text(
                                                "Modifier",
                                                modifier = Modifier.padding(start = 5.dp)
                                            )
                                        }
                                        DropdownMenuItem(
                                            onClick = {
                                                viewModel.markAsStarted(
                                                    onSuccess = {
                                                        dialogText = "Activité démarrée"
                                                        dialogIsSuccess = true
                                                    },
                                                    onError = {
                                                        dialogText =
                                                            "L'activité n'a pas pu être démarrée"
                                                        dialogIsSuccess = false
                                                    }
                                                )
                                            },
                                        ) {
                                            Icon(Icons.Outlined.PlayArrow, "Start")
                                            Text(
                                                "Débuter",
                                                modifier = Modifier.padding(start = 5.dp)
                                            )
                                        }
                                    }
                                    if (it.status == ActivityStatus.IN_PROGRESS) {
                                        DropdownMenuItem(
                                            onClick = {
                                                viewModel.markAsDone(
                                                    onSuccess = {
                                                        dialogText =
                                                            "Activité marquée comme terminée"
                                                        dialogIsSuccess = true
                                                    },
                                                    onError = {
                                                        dialogText =
                                                            "L'activité n'a pas pu être terminée"
                                                        dialogIsSuccess = false
                                                    }
                                                )
                                            },
                                        ) {
                                            Icon(Icons.Outlined.Done, "Done")
                                            Text(
                                                "Marquer comme faite",
                                                modifier = Modifier.padding(start = 5.dp)
                                            )
                                        }

                                        DropdownMenuItem(
                                            onClick = {
                                                viewModel.markAsUnDone(
                                                    onSuccess = {
                                                        dialogText =
                                                            "Activité marquée comme non terminée"
                                                        dialogIsSuccess = true
                                                    },
                                                    onError = {
                                                        dialogText =
                                                            "L'activité n'a pas pu être marquée comme non terminée"
                                                        dialogIsSuccess = false

                                                    }
                                                )
                                            },
                                        ) {
                                            Icon(Icons.Outlined.Undo, "Undone")
                                            Text(
                                                "Marquer comme non faite",
                                                modifier = Modifier.padding(start = 5.dp)
                                            )
                                        }
                                        DropdownMenuItem(
                                            onClick = {
                                                viewModel.markAsUnDone(
                                                    onSuccess = {
                                                        dialogText = "Activité annulée"
                                                        dialogIsSuccess = true
                                                    },
                                                    onError = {
                                                        dialogText =
                                                            "L'activité n'a pas pu être annulée"
                                                        dialogIsSuccess = false
                                                    }
                                                )
                                            },
                                        ) {
                                            Icon(Icons.Outlined.Cancel, "Cancel")
                                            Text(
                                                "Annuler",
                                                modifier = Modifier.padding(start = 5.dp)
                                            )
                                        }
                                    }
                                    if (listOf(
                                            ActivityStatus.INIT,
                                            ActivityStatus.DONE
                                        ).contains(it.status)
                                    ) {
                                        DropdownMenuItem(
                                            onClick = {
                                                viewModel.deleteActivity(
                                                    onSuccess = {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                "Activité supprimé"
                                                            )
                                                        }
                                                        navHostController.popBackStack()
                                                    },
                                                    onError = {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                "L'activité n'a pa pu etre supprimée\nRéessayer plus tard"
                                                            )
                                                        }
                                                    }
                                                )
                                            },
                                        ) {
                                            Icon(Icons.Outlined.Delete, "Delete")
                                            Text(
                                                "Supprimer",
                                                modifier = Modifier.padding(start = 5.dp)
                                            )
                                        }
                                    }

                                }
                            }

                        }

                        Card(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .size(200.dp)
                                .clip(CircleShape),
                            elevation = 8.dp
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Outlined.Timelapse, "Timelapse")
                                Text(text = "Duree Totale")
                                Text(
                                    text = (Duration.between(
                                        it.startDate.toInstant(),
                                        it.endDate.toInstant()
                                    ).seconds / (3600 * 24)).toInt().toString() + " Jours",
                                    style = MaterialTheme.typography.h1,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Badge(
                                    backgroundColor = MaterialTheme.colors.primary,
                                    contentColor = MaterialTheme.colors.background,
                                    modifier = Modifier.clip(CircleShape),
                                ) {
                                    Text(
                                        text = it.status.value,
                                        Modifier.padding(
                                            vertical = 5.dp,
                                            horizontal = 10.dp
                                        ),
                                        style = MaterialTheme.typography.body1,
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Debut",
                                    color = MaterialTheme.colors.background
                                )
                                Text(
                                    text = formatDateToHumanReadable(it.startDate),
                                    color = MaterialTheme.colors.background
                                )
                            }
                            Divider(
                                color = MaterialTheme.colors.background,
                                modifier = Modifier.width(100.dp)
                            )
                            Column {
                                Text(text = "Fin", color = MaterialTheme.colors.background)
                                Text(
                                    text = formatDateToHumanReadable(it.endDate),
                                    color = MaterialTheme.colors.background
                                )
                            }

                        }

                    }
                    var selectedIndex by remember {
                        mutableStateOf(0)
                    }
                    val tabs = listOf(
                        "Rizieres (${viewModel.activityPaddyFields.size})",
                        "Ouvriers (${viewModel.activityWorkers.size})",
                        "Ressources (${viewModel.activityResources.size})"
                    )
                    TabRow(
                        selectedTabIndex = selectedIndex,
                        backgroundColor = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    bottomEnd = 15.dp,
                                    bottomStart = 15.dp
                                )
                            )
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = selectedIndex == index,
                                onClick = { selectedIndex = index },
                            ) {
                                Text(
                                    text = tab,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (selectedIndex) {
                            0 -> {
                                var selectionMode by remember { mutableStateOf(false) }
                                LazyColumn(Modifier.fillMaxWidth()) {
                                    stickyHeader {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colors.background),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            if (selectionMode) {
                                                Row(verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .clickable {
                                                            if (viewModel.selectedActivityPaddyFields.isEmpty()) {
                                                                viewModel.selectedActivityPaddyFields =
                                                                    viewModel.activityPaddyFields
                                                            } else {
                                                                viewModel.selectedActivityPaddyFields =
                                                                    listOf()
                                                            }
                                                        }
                                                        .padding(
                                                            start = 15.dp,
                                                            end = 15.dp,
                                                            top = 5.dp,
                                                            bottom = 5.dp
                                                        )
                                                ) {
                                                    if (viewModel.selectedActivityPaddyFields.isEmpty()) {
                                                        Icon(
                                                            Icons.Outlined.Check,
                                                            "Tout selectionner",
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Text(
                                                            text = "Tout selectionner",
                                                            modifier = Modifier.padding(
                                                                start = 5.dp
                                                            ),
                                                            fontSize = 12.sp
                                                        )
                                                    } else {
                                                        Icon(
                                                            Icons.Outlined.CheckBox,
                                                            "Tout selectionner",
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Text(
                                                            text = "Tout deselectionner",
                                                            modifier = Modifier.padding(
                                                                start = 5.dp
                                                            ),
                                                            fontSize = 12.sp
                                                        )
                                                    }

                                                }

                                                Row(verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.clickable {
                                                        selectionMode = false
                                                        viewModel.selectedActivityPaddyFields =
                                                            listOf()
                                                    }) {
                                                    Icon(
                                                        Icons.Outlined.Cancel,
                                                        "cancel",
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Text("Annuler",
                                                        Modifier
                                                            .clickable {
                                                                selectionMode = false
                                                            }
                                                            .padding(start = 5.dp),
                                                        fontSize = 12.sp)
                                                }

                                            } else {
                                                Row(verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier
                                                        .clickable {
                                                            viewModel.activityPaddyFields
                                                                .sortedBy {
                                                                    it.paddyField.name
                                                                }
                                                                .also {
                                                                    viewModel.activityPaddyFields =
                                                                        it
                                                                }
                                                        }
                                                        .clip(RoundedCornerShape(15.dp))
                                                        .padding(
                                                            start = 15.dp,
                                                            end = 15.dp,
                                                            top = 5.dp,
                                                            bottom = 5.dp
                                                        )

                                                ) {
                                                    Icon(
                                                        Icons.Outlined.FilterList,
                                                        "Filter"
                                                    )
                                                    Text(text = "Filtrer", fontSize = 12.sp)
                                                }
                                                IconButton(onClick = {
                                                    scope.launch {
                                                        addPaddyFieldModalBottomSheetState.show()
                                                    }
                                                }) {
                                                    Icon(
                                                        Icons.Outlined.Add,
                                                        "Ajouter"
                                                    )
                                                }
                                            }

                                        }
                                    }
                                    items(viewModel.activityPaddyFields.size) { index ->
                                        Swipe(
                                            content = {
                                                Card(Modifier.fillMaxWidth()) {
                                                    Row(
                                                        Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        if (selectionMode)
                                                            Checkbox(
                                                                checked = viewModel.selectedActivityPaddyFields.contains(
                                                                    viewModel.activityPaddyFields[index]
                                                                ),
                                                                onCheckedChange = {
                                                                    if (it) {
                                                                        if (!viewModel.selectedActivityPaddyFields.contains(
                                                                                viewModel.activityPaddyFields[index]
                                                                            )
                                                                        ) {
                                                                            viewModel.selectedActivityPaddyFields += viewModel.activityPaddyFields[index]
                                                                        }
                                                                    } else {
                                                                        if (viewModel.selectedActivityPaddyFields.contains(
                                                                                viewModel.activityPaddyFields[index]
                                                                            )
                                                                        ) {
                                                                            viewModel.selectedActivityPaddyFields -= viewModel.activityPaddyFields[index]
                                                                        }
                                                                    }
                                                                })

                                                        PaddyFieldTile(
                                                            paddyField = viewModel.activityPaddyFields[index].paddyField,
                                                            onLongClick = {
                                                                selectionMode =
                                                                    !selectionMode
                                                                viewModel.selectedActivityPaddyFields =
                                                                    listOf()
                                                            },
                                                            badgeContent = {
                                                                Badge(
                                                                    backgroundColor = MaterialTheme.colors.primary,
                                                                    contentColor = MaterialTheme.colors.background,
                                                                    modifier = Modifier.clip(
                                                                        CircleShape
                                                                    ),
                                                                ) {
                                                                    Text(
                                                                        text = viewModel.activityPaddyFields[index].status.value,
                                                                        Modifier.padding(
                                                                            vertical = 5.dp,
                                                                            horizontal = 10.dp
                                                                        ),
                                                                        style = MaterialTheme.typography.body1,
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    }

                                                }
                                            },
                                            leftContent = {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(MaterialTheme.colors.primary)
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .align(Alignment.CenterStart)
                                                            .padding(start = 15.dp)
                                                    ) {
                                                        when (viewModel.activityPaddyFields[index].status) {
                                                            ActivityStatus.INIT -> {
                                                                Icon(
                                                                    imageVector = Icons.Default.PlayArrow,
                                                                    contentDescription = null,
                                                                    tint = MaterialTheme.colors.background,
                                                                    modifier = Modifier.align(
                                                                        Alignment.CenterHorizontally
                                                                    )
                                                                )
                                                                Spacer(
                                                                    modifier = Modifier.heightIn(
                                                                        5.dp
                                                                    )
                                                                )
                                                                Text(
                                                                    text = "Demarrer",
                                                                    textAlign = TextAlign.Center,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = MaterialTheme.colors.background
                                                                )
                                                            }
                                                            ActivityStatus.IN_PROGRESS -> {
                                                                Icon(
                                                                    imageVector = Icons.Default.Done,
                                                                    contentDescription = null,
                                                                    tint = MaterialTheme.colors.background,
                                                                    modifier = Modifier.align(
                                                                        Alignment.CenterHorizontally
                                                                    )
                                                                )
                                                                Spacer(
                                                                    modifier = Modifier.heightIn(
                                                                        5.dp
                                                                    )
                                                                )
                                                                Text(
                                                                    text = "Terminer",
                                                                    textAlign = TextAlign.Center,
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = MaterialTheme.colors.background
                                                                )
                                                            }
                                                            else -> {

                                                            }
                                                        }
                                                    }
                                                }
                                            },
                                            rightContent = {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(MaterialTheme.colors.error)
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .align(Alignment.CenterEnd)
                                                            .padding(end = 15.dp)
                                                    ) {
                                                        when (viewModel.activityPaddyFields[index].status) {
                                                            ActivityStatus.INIT -> {
                                                                Icon(
                                                                    imageVector = Icons.Default.Delete,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.align(
                                                                        Alignment.CenterHorizontally
                                                                    )
                                                                )
                                                                Spacer(
                                                                    modifier = Modifier.heightIn(
                                                                        5.dp
                                                                    )
                                                                )
                                                                Text(
                                                                    text = "Supprimer",
                                                                    textAlign = TextAlign.Center,
                                                                    fontWeight = FontWeight.Bold,
                                                                )
                                                            }
                                                            ActivityStatus.IN_PROGRESS -> {
                                                                Icon(
                                                                    imageVector = Icons.Default.Delete,
                                                                    contentDescription = null,
                                                                    modifier = Modifier.align(
                                                                        Alignment.CenterHorizontally
                                                                    )
                                                                )
                                                                Spacer(
                                                                    modifier = Modifier.heightIn(
                                                                        5.dp
                                                                    )
                                                                )
                                                                Text(
                                                                    text = "Non Fait",
                                                                    textAlign = TextAlign.Center,
                                                                    fontWeight = FontWeight.Bold,
                                                                )
                                                            }
                                                            else -> {

                                                            }
                                                        }

                                                    }
                                                }
                                            },
                                            onSwipeToLeft = {

                                            },
                                            onSwipeToRight = {

                                            }
                                        )
                                    }
                                }
                            }
                            1 -> {
                                var selectionMode by remember { mutableStateOf(false) }
                                LazyColumn(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    stickyHeader {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal = 12.dp,
                                                    vertical = 10.dp
                                                )
                                                .background(MaterialTheme.colors.background),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            if (selectionMode) {

                                                Row(verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.clickable {
                                                        if (viewModel.selectedActivityWorkers.isEmpty()) {
                                                            viewModel.selectedActivityWorkers =
                                                                viewModel.activityWorkers
                                                        } else {
                                                            viewModel.selectedActivityWorkers =
                                                                listOf()
                                                        }
                                                    }
                                                ) {
                                                    if (viewModel.selectedActivityWorkers.isEmpty()) {
                                                        Icon(
                                                            Icons.Outlined.Check,
                                                            "Tout selectionner",
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Text(
                                                            text = "Tout selectionner",
                                                            modifier = Modifier.padding(
                                                                start = 5.dp
                                                            ),
                                                            fontSize = 12.sp
                                                        )
                                                    } else {
                                                        Icon(
                                                            Icons.Outlined.CheckBox,
                                                            "Tout selectionner",
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Text(
                                                            text = "Tout deselectionner",
                                                            modifier = Modifier.padding(
                                                                start = 5.dp
                                                            ),
                                                            fontSize = 12.sp
                                                        )
                                                    }

                                                }
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        Icons.Outlined.RemoveFromQueue,
                                                        "remove",
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Text(
                                                        text = "Retirer la selection",
                                                        modifier = Modifier.padding(start = 5.dp),
                                                        fontSize = 12.sp
                                                    )
                                                }

                                                Row(verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.clickable {
                                                        selectionMode = false
                                                        viewModel.selectedActivityWorkers =
                                                            listOf()
                                                    }) {
                                                    Icon(
                                                        Icons.Outlined.Cancel,
                                                        "cancel",
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Text("Annuler",
                                                        Modifier
                                                            .clickable {
                                                                selectionMode = false
                                                            }
                                                            .padding(start = 5.dp),
                                                        fontSize = 12.sp)
                                                }

                                            } else {
                                                Row(verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier
                                                        .clickable {
                                                            viewModel.selectedActivityWorkers.sortedBy {
                                                                it.worker.name
                                                            }
                                                        }
                                                        .clip(RoundedCornerShape(15.dp))
                                                        .padding(
                                                            start = 15.dp,
                                                            end = 15.dp,
                                                            top = 5.dp,
                                                            bottom = 5.dp
                                                        )

                                                ) {
                                                    Icon(
                                                        Icons.Outlined.FilterList,
                                                        "Filter"
                                                    )
                                                    Text(text = "Filtrer", fontSize = 12.sp)
                                                }
                                                IconButton(onClick = { scope.launch { addWorkersModalBottomSheetState.show() } }) {
                                                    Icon(
                                                        Icons.Outlined.Add,
                                                        "Ajouter"
                                                    )
                                                }
                                            }

                                        }
                                    }
                                    items(viewModel.activityWorkers.size) { index ->
                                        Card(
                                            Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Row(
                                                Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (selectionMode)
                                                    Checkbox(
                                                        checked = viewModel.selectedActivityWorkers.contains(
                                                            viewModel.activityWorkers[index]
                                                        ),
                                                        onCheckedChange = {
                                                            if (it) {
                                                                if (!viewModel.selectedActivityWorkers.contains(
                                                                        viewModel.activityWorkers[index]
                                                                    )
                                                                ) {
                                                                    viewModel.selectedActivityWorkers += viewModel.activityWorkers[index]
                                                                }
                                                            } else {
                                                                if (viewModel.selectedActivityWorkers.contains(
                                                                        viewModel.activityWorkers[index]
                                                                    )
                                                                ) {
                                                                    viewModel.selectedActivityWorkers -= viewModel.activityWorkers[index]
                                                                }
                                                            }
                                                        })
                                                WorkerTile(
                                                    worker = viewModel.activityWorkers[index].worker,
                                                    onLongClick = {
                                                        selectionMode = !selectionMode
                                                        viewModel.selectedActivityWorkers =
                                                            listOf()
                                                    }
                                                )
                                            }

                                        }
                                    }
                                }

                            }
                            2 -> {
                                LazyColumn(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    items(viewModel.activityResources.size) { index ->
                                        Card(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                                .clickable { }) {
                                            Column(Modifier.fillMaxWidth()) {
                                                ResourceTile(
                                                    resource = viewModel.activityResources[index].resource,
                                                )
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            bottom = 10.dp,
                                                            start = 10.dp,
                                                            end = 10.dp
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        "Quantite utilisee : ${viewModel.activityResources[index].quantity}",
                                                        style = MaterialTheme.typography.body1,
                                                        modifier = Modifier.padding(start = 20.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        "Cout : ${viewModel.activityResources[index].value} FCFA",
                                                        style = MaterialTheme.typography.body1,
                                                        modifier = Modifier.padding(start = 20.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                AddPaddyFieldModalBottomSheet(
                    modalBottomSheetState = addPaddyFieldModalBottomSheetState,
                    viewModel = remember {
                        AddPaddyFieldViewModel(
                            alreadyExists = viewModel.activityPaddyFields,
                            onAddPaddyFields = { addedActivityPaddyFields ->
                                viewModel.activityPaddyFields += addedActivityPaddyFields
                                scope.launch {
                                    addPaddyFieldModalBottomSheetState.hide()
                                }
                            },
                            activityCode = it.code
                        )
                    }
                )
                AddWorkersModalBottomSheet(
                    modalBottomSheetState = addWorkersModalBottomSheetState,
                    viewModel = remember {
                        AddWorkersViewModel(
                            alreadyExists = viewModel.activityWorkers,
                            onAddWorkersToActivity = { addedWorkers ->
                                viewModel.activityWorkers += addedWorkers
                                scope.launch {
                                    addWorkersModalBottomSheetState.hide()
                                }
                            },
                            activityCode = it.code
                        )
                    }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Swipe(
    content: @Composable () -> Unit,
    onSwipeToLeft: () -> Unit = {},
    onSwipeToRight: () -> Unit = {},
    leftContent: @Composable () -> Unit = {},
    rightContent: @Composable () -> Unit = {}
) {
    val dismissState = rememberDismissState(initialValue = DismissValue.Default)
    val scope = rememberCoroutineScope()
    SwipeToDismiss(
        state = dismissState,
        background = {
            when (dismissState.dismissDirection) {
                DismissDirection.EndToStart -> {
                    rightContent()
                    onSwipeToLeft().also {
                        scope.launch {
                            dismissState.reset()
                        }
                    }
                }
                DismissDirection.StartToEnd -> {
                    leftContent()
                    onSwipeToRight().also {
                        scope.launch {
                            dismissState.reset()
                        }
                    }
                }
                else -> {
                    content()
                }
            }
        },
        dismissContent = { content() },
        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
    )


}