package fr.gilles.riceattend.ui.screens.main.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.ui.viewmodel.WorkerVM
import fr.gilles.riceattend.ui.widget.components.ActivityTile
import fr.gilles.riceattend.ui.widget.components.AppBar
import fr.gilles.riceattend.ui.widget.components.LoadingCard
import fr.gilles.riceattend.ui.widget.components.WorkerForm
import kotlinx.coroutines.launch


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
            viewModel.worker?.let {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item{
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
                                IconButton(onClick = {}) {

                                }
                            }
                        )
                    }

                   item{
                       Card(
                           Modifier
                               .fillMaxWidth()
                               .padding(10.dp)
                       ) {
                           Row(
                               Modifier
                                   .fillMaxWidth()
                                   .padding(10.dp),
                               horizontalArrangement = Arrangement.Start,
                               verticalAlignment = Alignment.CenterVertically
                           ) {
                               Card(
                                   modifier = Modifier
                                       .padding(end = 20.dp, start = 10.dp)
                                       .width(100.dp)
                                       .height(100.dp)
                                       .clip(CircleShape)
                               ) {
                                   Icon(Icons.Outlined.AccountCircle, "Worker image")
                               }
                               Column {
                                   Text(
                                       text = it.name,
                                       fontWeight = FontWeight.Bold,
                                       modifier = Modifier.padding(bottom = 7.dp)
                                   )
                                   Text(
                                       text = it.phone,
                                       fontWeight = FontWeight.Bold,
                                       modifier = Modifier
                                           .padding(bottom = 7.dp)
                                           .clickable { viewModel.openDialer() }
                                   )
                                   Text(
                                       text = it.email,
                                       modifier = Modifier
                                           .padding(bottom = 10.dp)
                                           .clickable { viewModel.openMail() },
                                   )

                               }

                           }
                       }
                   }
                    item{
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Button(onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 3.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    "Edit",
                                )
                                Text(text = "Modifier")
                            }

                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 3.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Red,
                                    contentColor = Color.White)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete",
                                )
                                Text(text = "Supprimer")
                            }
                        }
                    }

                   item {
                       Row(
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(horizontal = 15.dp),
                       Arrangement.SpaceBetween,
                       Alignment.CenterVertically
                   ) {
                       Text(
                           text = "Activites",
                       )
                       IconButton(onClick = {
                           it.activityWorkers.sortedBy { activityWorker -> activityWorker.price }
                               .also { result -> it.activityWorkers = result }
                       }) {
                           Icon(Icons.Outlined.FilterList, "Filter")
                       }
                   } }
                    items(it.activityWorkers.size) { index ->
                        Box(Modifier.padding(horizontal = 10.dp)) {
                            ActivityTile(
                                activity = it.activityWorkers[index].activity
                            ) {
                                Text(text = "Revenu: ${it.activityWorkers[index].price} FCFA")
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

