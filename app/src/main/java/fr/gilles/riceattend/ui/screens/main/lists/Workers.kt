package fr.gilles.riceattend.ui.screens.main.lists

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.main.Drawer
import fr.gilles.riceattend.ui.viewmodel.WorkersVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun WorkersFragment(
    navHostController: NavHostController,
    viewModel: WorkersVM = hiltViewModel()
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var created by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    if (created) OpenDialog(
        title = "Ajouter un ouvrier",
        onDismiss = { created = false },
        onConfirm = { created = false; },
        show = created,
        content = { Text(text = "Ouvrier ajouté avec succès") },
        isSuccess = true
    )
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
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.FilterList, "Add",)
                }
            })
        },
        drawerGesturesEnabled = true,
        content = { padding->
            when (viewModel.loading) {
                true -> {
                    LoadingCard()
                }
                false -> {
                    viewModel.workers?.let {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = if (it.empty == true) Arrangement.Center else Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (it.empty == true)
                                Text(
                                    "Aucune ouvrier trouvé",
                                    style = MaterialTheme.typography.body1
                                )
                            else it.content.forEach { worker ->
                                Box(Modifier.padding(horizontal = 10.dp)) {
                                    WorkerTile(worker, withBadge = true, onClick = {
                                        navHostController.navigate(
                                            Route.WorkerRoute.path.replace(
                                                "{code}",
                                                worker.code
                                            )
                                        ) {}

                                    })
                                }
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Outlined.Add, "Add")
            }
        },
    )
    ModalBottomSheetLayout(
        sheetContent = {
            WorkerForm(
                workerFormVM = viewModel.workerFormVM,
                onSubmit = {
                    viewModel.create(
                        onSuccess = { created = true },
                        onError = { scope.launch { snackbarHostState.showSnackbar(it) } }
                    )
                }, isLoading = viewModel.creationLoading
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}
}

