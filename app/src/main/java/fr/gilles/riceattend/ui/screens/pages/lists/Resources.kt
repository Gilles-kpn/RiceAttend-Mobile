package fr.gilles.riceattend.ui.screens.pages.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import fr.gilles.riceattend.misc.OnBottomReached
import fr.gilles.riceattend.models.ResourceType
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.pages.Drawer
import fr.gilles.riceattend.ui.viewmodel.ResourcesVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun ResourcesFragment(
    navHostController: NavHostController = rememberNavController(),
    viewModel: ResourcesVM = hiltViewModel()
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var created by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(navHostController, scope, scaffoldState) },
        topBar = {
            AppBar(title = "Ressources",
                leftContent = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Outlined.Menu, "Menu", )
                    }
                }, rightContent = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Outlined.Refresh, "refresh", )
                    }
                })
        },
        drawerGesturesEnabled = true,
        content = { padding->
            if (created) OpenDialog(title = "Ajouter une ressources",
                onDismiss = { created = false },
                onConfirm = { created = false; },
                show = created,
                content = { Text(text = "Nouvelle ressource enregistre avec success") }, isSuccess = true)
            when (viewModel.loading) {
                true -> {
                    LoadingCard()
                }
                false -> {
                    viewModel.resources?.let {
                        val listState = rememberLazyListState()
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            state = listState,
                            verticalArrangement = if (it.empty == true) Arrangement.Center else Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (it.empty == true && it.first == true)
                            item{
                                Text(text = "Aucune ressource trouvée")
                            }else{
                                it.forEach { resource ->
                                    item {
                                        Box (Modifier.padding(horizontal = 10.dp)){
                                            ResourceTile(resource = resource, onClick = {
                                                navHostController.navigate(
                                                    Route.ResourceRoute.path.replace(
                                                        "{code}",
                                                        resource.code
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
                    }?: run{
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
        }
    )


    ModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                val types = listOf(
                    ResourceType.WATER,
                    ResourceType.OTHER,
                    ResourceType.FERTILIZER,
                    ResourceType.MATERIALS
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Agriculture, "Resource")
                    Text(text = "Creer une ressource")
                }
                InputWidget(state = viewModel.resourceFormVM.name, title = "Nom")
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        InputNumberWidget(
                            state = viewModel.resourceFormVM.quantity,
                            title = "Quantite",
                            icon = Icons.Outlined.Countertops
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        InputNumberWidget(
                            state = viewModel.resourceFormVM.price,
                            title = "Prix Unitaire",
                            icon = Icons.Outlined.PriceChange
                        )
                    }
                }
               Box(Modifier.fillMaxWidth()) {
                   InputDropDownSelect(
                       state = viewModel.resourceFormVM.type,
                       list = types,
                       template = {
                           Text(text = it.label, modifier = Modifier.padding(10.dp))
                       },
                       title = "Type de ressource"
                   )
               }
                Button(
                    enabled = !viewModel.resourceFormVM.loading && (
                            viewModel.resourceFormVM.name.isValid() &&
                                    viewModel.resourceFormVM.quantity.isValid() &&
                                    viewModel.resourceFormVM.type.isValid()
                            ),
                    onClick = {
                        viewModel.create({
                          scope.launch {
                                modalBottomSheetState.hide()
                          }
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(50.dp)
                ) {
                    if (viewModel.resourceFormVM.loading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Enregistrer", style = MaterialTheme.typography.button)
                    }
                }
            }

        },
        sheetState = modalBottomSheetState,
    ) {}

}

