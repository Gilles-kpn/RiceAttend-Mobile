package fr.gilles.riceattend.ui.screens.pages.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.viewmodel.RessourceVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResourcePage(
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: RessourceVM
) {

    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    when (viewModel.loading) {
        true -> {
            LoadingCard()
        }
        false -> {
            viewModel.resource?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())

                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppBar(
                        title = it.name,
                        leftContent = {
                            IconButton(
                                onClick = {
                                    navHostController.popBackStack()
                                },
                            ) {
                                Icon(
                                    Icons.Outlined.ArrowBack,
                                    "Back",
                                )
                            }
                        },
                        rightContent = {
                            IconButton(onClick = { scope.launch { modalBottomSheetState.show() } }) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    "Update",
                                )
                            }
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Informations de la ressource",
                            Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                            fontSize = 12.sp
                        )
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Nom")
                                    Text(text = it.name, fontWeight = FontWeight.SemiBold)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Type")
                                    Text(
                                        text = it.resourceType.label,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Quantité restante")
                                    Text(
                                        text = it.quantity.toString(),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Prix Unitaire")
                                    Text(
                                        text = it.unitPrice.toString() + " FCFA",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Quantité utilisée")
                                    Text(
                                        text = "${it.activityResources.map { it.quantity }.sum().toInt()}",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Valeur utilisée")
                                    Text(
                                        text = "${it.activityResources.map { it.quantity }.sum().toInt() * it.unitPrice} FCFA",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                        Button(onClick = { scope.launch{ modalBottomSheetState.show() } },
                            Modifier
                                .fillMaxWidth()) {
                            Text(text = "Se Réapprovisionner", modifier = Modifier.padding(vertical = 5.dp))
                        }
                        Text(
                            text = "Activités utilisant cette ressource",
                            Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                            fontSize = 12.sp
                        )
                        it.activityResources.forEach { activityPaddyField ->
                            ActivityTile(activity = activityPaddyField.activity, onClick = {
                                navHostController.navigate(
                                    Route.ActivityRoute.path.replace(
                                        "{code}",
                                        activityPaddyField.activity.code
                                    )
                                ) {
                                }
                            })
                        }
                        if (it.activityResources.isEmpty()){
                            EmptyCard()
                        }
                    }
                }
            }

        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
           Column(modifier = Modifier.padding(horizontal = 10.dp)) {
               Text(text = "Entrez la quantite a ajouter", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(vertical = 10.dp))
               InputNumberWidget(state = viewModel.quantityToAdd, title = "Quantité")
           }
            Button(
                enabled = viewModel.quantityToAdd.isValid(),
                onClick = { viewModel.addQuantity( onSuccess = {
                    scope.launch {
                        modalBottomSheetState.hide()
                        snackbarHostState.showSnackbar("Ressource reapprovisionnée")
                    }
                }, onError = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Une erreur est survenue")
                    }
                }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(50.dp)
            ) {
                if (viewModel.addLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Valider", style = MaterialTheme.typography.button)
                }
            }

        },
        sheetState = modalBottomSheetState,
    ) {}


}