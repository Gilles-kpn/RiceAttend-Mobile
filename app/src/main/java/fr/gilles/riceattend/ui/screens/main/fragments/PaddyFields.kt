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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.viewmodel.PaddyFieldsVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun PaddyFieldsFragment(
    onMenuClick: () -> Unit = {},
    viewModel: PaddyFieldsVM = viewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState,
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var created by remember { mutableStateOf(false) }
    if (created)
        OpenDialog(
            title = "Riziere creee",
            onDismiss = { created = false },
            onConfirm = { created = false; },
            show = created,
            content = {
                Text("Riziere creee avec succes")
            }
        )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBar(title = "Rizieres", leftContent = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.Menu, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = { scope.launch { modalBottomSheetState.show() } }) {
                Icon(Icons.Outlined.Add, "Add", tint = Color.White)
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
                    viewModel.paddyFields?.let {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = if (it.empty == true) Arrangement.Center else Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (it.empty == true)
                                Text(
                                    "Aucune riziere trouvé",
                                    style = MaterialTheme.typography.body1
                                )
                            else {

                                it.content.forEach { paddyField ->
                                    PaddyFieldTile(paddyField, onClick = {
                                        navHostController.navigate(
                                            Route.PaddyFieldRoute.path.replace(
                                                "{code}",
                                                paddyField.code
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

    }
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





