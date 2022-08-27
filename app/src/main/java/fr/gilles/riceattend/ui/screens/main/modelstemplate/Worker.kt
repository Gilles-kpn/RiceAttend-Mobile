package fr.gilles.riceattend.ui.screens.main.modelstemplate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.ui.viewmodel.WorkerVM
import fr.gilles.riceattend.ui.widget.components.ActivityTile
import fr.gilles.riceattend.ui.widget.components.AppBar
import fr.gilles.riceattend.ui.widget.components.LoadingCard
import fr.gilles.riceattend.ui.widget.components.WorkerForm
import kotlinx.coroutines.launch
import java.time.Duration


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun WorkerPage(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: WorkerVM
) {
    val scope = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    when (viewModel.loading) {
        true -> {
            LoadingCard()
        }
        false -> {
            when (viewModel.worker) {
                null -> {}
                else -> {
                    viewModel.worker?.let {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
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
                                    IconButton(onClick = {}) {

                                    }
                                }
                            )

                            Card(
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 20.dp)
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clip(CircleShape)
                            ) {
                                Icon(Icons.Outlined.AccountCircle, "Worker image")
                            }
                            Text(
                                text = it.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                            Text(
                                text = it.phone,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = 7.dp)
                                    .clickable { viewModel.openDialer() }
                            )
                            Text(
                                text = it.email,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .clickable { viewModel.openMail() },
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                            ) {
                                IconButton(
                                    onClick = { /*TODO*/ }, modifier = Modifier
                                        .clip(CircleShape)
                                ) {
                                    Icon(Icons.Outlined.Assignment, "Add to activity")
                                }
                                IconButton(
                                    onClick = { scope.launch { modalBottomSheetState.show() } },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colors.background)
                                ) {
                                    Icon(
                                        Icons.Outlined.Edit,
                                        "Edit",
                                        tint = MaterialTheme.colors.secondary
                                    )
                                }

                                IconButton(
                                    onClick = { /*TODO*/ }, modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colors.background)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colors.error
                                    )
                                }


                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                Arrangement.SpaceBetween,
                                Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Activites",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = {
                                    viewModel.workerActivities.sortedBy { activityWorker ->
                                        activityWorker.price
                                    }.also { viewModel.workerActivities = it }
                                }) {
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
                                LazyColumn(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(bottom = 8.dp)){
                                    items(viewModel.workerActivities.size){ index ->
                                        Card(modifier = Modifier
                                            .fillMaxWidth()
                                            .animateItemPlacement(
                                                tween(durationMillis = 250)
                                            )
                                            .clickable { }){
                                            Column(modifier = Modifier.padding( bottom = 5.dp)) {
                                                ActivityTile(activity = viewModel.workerActivities[index].activity)
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 20.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(text = "Duree : ${
                                                        (Duration.between(
                                                            viewModel.workerActivities[index].activity.startDate.toInstant(),
                                                            viewModel.workerActivities[index].activity.endDate.toInstant()
                                                        ).seconds / (3600 * 24)).toInt()} Jours", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                                                    Text(text= "Paye : ${viewModel.workerActivities[index].price} FCFA", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                                }
                                            }
                                        }
                                        Divider()
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
            WorkerForm(
                onSubmit = {
                    viewModel.updateWorker {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Une erreur est survenue\n$it",
                            )
                        }
                    }
                },
                title = "Modifier les informations",
                workerFormVM = viewModel.workerFormVM,
                buttonText = "Modifier",
                isLoading = viewModel.updateLoading
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}
}

