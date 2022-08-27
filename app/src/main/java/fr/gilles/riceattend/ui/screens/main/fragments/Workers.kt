package fr.gilles.riceattend.ui.screens.main.fragments

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.viewmodel.WorkersVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun WorkersFragment(
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController,
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState,
    viewModel: WorkersVM = WorkersVM()
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var created by remember { mutableStateOf(false) }
    if (created) OpenDialog(
        title = "Ajouter un ouvrier",
        onDismiss = { created = false },
        onConfirm = { created = false; },
        show = created,
        content = { Text(text = "Ouvrier ajouté avec succès") })
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBar(title = "Ouvriers", leftContent = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.Menu, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = { scope.launch { modalBottomSheetState.show() } }) {
                Icon(
                    Icons.Outlined.Add,
                    "More",
                    tint = MaterialTheme.colors.background
                )
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.FilterList, "Filtrer")
                    Text("Filtrer", modifier = Modifier.padding(10.dp))
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
                  viewModel.workers?.let {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
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
                                        WorkerTile(worker, onClick = {
                                            navHostController.navigate(
                                                Route.WorkerRoute.path.replace(
                                                    "{code}",
                                                    worker.code
                                                )
                                            ) {}

                                        })
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                        )
                                    }
                                }
                            }
                }
            }
        }
    }
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

