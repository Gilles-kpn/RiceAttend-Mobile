package fr.gilles.riceattend.ui.screens.main.modelstemplate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
                    modifier = Modifier.fillMaxSize(),
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
                                    tint = MaterialTheme.colors.background
                                )
                            }
                        },
                        rightContent = {
                            IconButton(onClick = { scope.launch { modalBottomSheetState.show() } }) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    "Update",
                                    tint = MaterialTheme.colors.background
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
                            text = it.name,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(10.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            var expanded by remember { mutableStateOf(false) }
                            AsyncImage(
                                model = viewModel.paddyField!!.plant.image,
                                contentDescription = "Profile picture",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color.Black),
                                            startY = if (expanded) 100f else 300f,
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {

                                Column(Modifier.fillMaxWidth()) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = it.plant.name,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        IconButton(onClick = { expanded = !expanded }) {
                                            Icon(
                                                Icons.Outlined.ArrowDropDown,
                                                contentDescription = "View more",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                    androidx.compose.animation.AnimatedVisibility(visible = expanded) {
                                        Text(
                                            text = it.plant.description,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
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
                                    Icons.Outlined.Filter2,
                                    contentDescription = "Number of plants"
                                )
                                Text(
                                    text = "Nombre de plant",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = it.numberOfPlants.toString() + " plants",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
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
                                    contentDescription = "Supperfici"
                                )
                                Text(
                                    text = "Superficie",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = it.surface.value.toString() + " " + it.surface.unit,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Activites sur cette riziere",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { /*TODO*/ }) {
                                //filter
                                Icon(Icons.Outlined.FilterList, "Filter")
                            }
                        }
                        Divider(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LazyColumn(Modifier.fillMaxSize()){
                                items(viewModel.paddyFieldActivities.size){
                                    index ->
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ){
                                        ActivityTile(activity = viewModel.paddyFieldActivities[index].activity)
                                    }
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

