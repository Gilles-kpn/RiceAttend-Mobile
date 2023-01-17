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
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.misc.OnBottomReached
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.pages.Drawer
import fr.gilles.riceattend.ui.viewmodel.PaddyFieldsVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
@Preview
fun PaddyFieldsFragment(
    viewModel: PaddyFieldsVM = hiltViewModel(),
    navHostController: NavHostController = rememberNavController(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val snackBarHostState = remember { SnackbarHostState() }
    var created by remember { mutableStateOf(false) }
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(navHostController, scope, scaffoldState) },
        topBar = {
            AppBar(title = "Rizieres",
                leftContent = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Outlined.Menu, "Menu")
                    }
                }, rightContent = {
                    IconButton(onClick = { viewModel.refresh()}) {
                        Icon(Icons.Outlined.Refresh, "refresh")
                    }
                })
        },
        drawerGesturesEnabled = true,
        content = { padding ->

            if (created)
                OpenDialog(
                    title = "Riziere creee",
                    onDismiss = { created = false },
                    onConfirm = { created = false; },
                    show = created,
                    content = {
                        Text("Riziere creee avec succes")
                    },
                    isSuccess = true
                )
            when (viewModel.loading) {
                true -> {
                    LoadingCard()
                }
                false -> {
                    viewModel.paddyFields?.let {
                        val listState = rememberLazyListState()
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = if (it.empty == true) Arrangement.Center else Arrangement.Top,
                            state = listState
                        ) {
                            if (it.empty == true && it.first == true)
                                item {
                                    Text(
                                        "Aucune riziere trouvé",
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            else {
                                it.forEach { paddyField ->
                                    item {
                                        PaddyFieldTile(paddyField, onClick = {
                                            navHostController.navigate(
                                                Route.PaddyFieldRoute.path.replace(
                                                    "{code}",
                                                    paddyField.code
                                                )
                                            )
                                        })
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
            FloatingActionButton(onClick = {
                scope.launch {
                    modalBottomSheetState.show()
                }
            }) {
                Icon(Icons.Outlined.Add, "Add")
            }
        },
    )
    ModalBottomSheetLayout(
        sheetContent = {
            PaddyFieldForm(
                title = "Créer une rizière",
                paddyFormViewModel = viewModel.paddyFormViewModel,
                plants = viewModel.plants,
                onClick = {
                    viewModel.createPaddyField(
                        onError = {
                            scope.launch {
                                snackBarHostState.showSnackbar(it)
                            }
                        },
                        onSuccess = { created = true }
                    )
                },
                isLoading = viewModel.paddyFieldCreationLoading,
                buttonText = "Enregistrer",

                )
        },
        sheetState = modalBottomSheetState,
    ) {}


}





