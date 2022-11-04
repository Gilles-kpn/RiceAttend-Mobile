package fr.gilles.riceattend.ui.screens.main.lists

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.main.Drawer
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
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
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
                IconButton(onClick = {  }) {
                    Icon(Icons.Outlined.FilterList, "Add")
                }
            })
        },
        drawerGesturesEnabled = true,
        content = {padding ->

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
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            items(it.content.size){
                                    index ->  PaddyFieldTile(it.content[index], onClick = {
                                navHostController.navigate(
                                    Route.PaddyFieldRoute.path.replace(
                                        "{code}",
                                        it.content[index].code
                                    )
                                ) {}
                            })
                            }
                            if (it.empty == true)
                                item {
                                    Text(
                                        "Aucune riziere trouvé",
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                        }
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
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}



}





