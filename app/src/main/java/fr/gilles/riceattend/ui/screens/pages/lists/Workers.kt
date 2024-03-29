package fr.gilles.riceattend.ui.screens.pages.lists

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fr.gilles.riceattend.misc.OnBottomReached
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.pages.Drawer
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
            AppBar(title = "Ouvriers", leftContent = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Outlined.Menu, "Menu",)
                }
            }, rightContent = {
                IconButton(onClick = { viewModel.refresh() }) {
                    Icon(Icons.Outlined.Refresh, "refresh",)
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
                        val listState = rememberLazyListState()
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            state = listState,
                            verticalArrangement = if (it.empty == true) Arrangement.Center else Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (it.empty == true && it.first == true)
                               item {
                                   Text(
                                       "Aucune ouvrier trouvé",
                                       style = MaterialTheme.typography.body1
                                   )
                               }
                            else {
                                it.forEach { worker ->
                                    item {
                                        Box(Modifier.padding(horizontal = 10.dp)) {
                                            WorkerTile(worker, withBadge = true, onClick = {
                                                navHostController.navigate(
                                                    Route.WorkerRoute.path.replace(
                                                        "{code}",
                                                        worker.code
                                                    )
                                                )
                                            })
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

                        }
                        listState.OnBottomReached {
                            viewModel.viewMore()
                        }

                    } ?: run{
                        EmptyCard()
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
                        onSuccess = {
                            scope.launch {
                                modalBottomSheetState.hide();
                            }
                                    },
                        onError = { scope.launch { snackbarHostState.showSnackbar(it) } }
                    )
                }, isLoading = viewModel.creationLoading
            )
        },
        sheetState = modalBottomSheetState,
    ) {}
}

