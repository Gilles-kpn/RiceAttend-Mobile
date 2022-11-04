package fr.gilles.riceattend.ui.screens.main.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import fr.gilles.riceattend.ui.viewmodel.PaddyFieldVM
import fr.gilles.riceattend.ui.widget.components.ActivityTile
import fr.gilles.riceattend.ui.widget.components.AppBar
import fr.gilles.riceattend.ui.widget.components.LoadingCard
import fr.gilles.riceattend.ui.widget.components.PaddyFieldForm
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaddyFieldPage(
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: PaddyFieldVM
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
            viewModel.paddyField?.let {
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
                            text = "Informations du plant",
                            Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                            fontSize = 12.sp

                        )
                        Card(Modifier.padding(10.dp)){
                            Column{
                                AsyncImage(
                                    model = it.plant.image,
                                    contentDescription = "plant",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )

                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Text("Nom du plant", fontWeight = FontWeight.SemiBold,  fontSize = 12.sp)
                                    Text(it.plant.name,  fontSize = 12.sp)
                                }
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Text("Forme du plant" ,fontWeight = FontWeight.SemiBold,  fontSize = 12.sp )
                                    Text(
                                        it.plant.shape,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Informations de la rizière, ",
                            Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                            fontSize = 12.sp
                        )
                        Card(Modifier.padding(10.dp)) {
                            Column{
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Outlined.ConfirmationNumber,
                                            contentDescription = "Number of plants",
                                            modifier = Modifier
                                                .padding(horizontal = 5.dp)
                                                .size(16.dp)
                                        )
                                        Text(
                                            text = "Nombre de plants",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Text(
                                        text = it.numberOfPlants.toString() + " plants",
                                        fontSize = 12.sp
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Outlined.AreaChart,
                                            contentDescription = "area",
                                            modifier = Modifier
                                                .padding(horizontal = 5.dp)
                                                .size(16.dp)
                                        )
                                        Text(
                                            text = "Superficie",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Text(
                                        text = it.surface.value.toString() + " " + it.surface.unit,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Activités sur cette rizière",
                            Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                            fontSize = 12.sp
                        )
                        it.activityPaddyFields.forEach { activityPaddyField ->
                            ActivityTile(activity = activityPaddyField.activity) {}
                        }
                    }
                }
            }

        }
    }
    ModalBottomSheetLayout(
        sheetContent = {
            PaddyFieldForm(
                title = "Modifier la riziere",
                paddyFormViewModel = viewModel.paddyfieldFormVM,
                plants = viewModel.plants,
                onClick = {
                    viewModel.update(onError = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Une erreur est survenue \n$it",
                            )
                        }
                    })
                },
                isLoading = viewModel.updateLoading,
                buttonText = "Modifier",

                )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}
}

