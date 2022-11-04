package fr.gilles.riceattend.ui.widget.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.ui.navigation.addPaddyFieldViewModel
import fr.gilles.riceattend.ui.navigation.addWorkerViewModel
import fr.gilles.riceattend.ui.viewmodel.ActivityVM
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActivityOptions(
    viewModel: ActivityVM,
    state: ModalBottomSheetState
){
    val scope = rememberCoroutineScope()
    val paddyFieldState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val workerState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Column {
                Text(text = "Options", modifier = Modifier.padding(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable {
                            scope.launch {
                                state.hide()
                                paddyFieldState.show()
                            }
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Outlined.Landscape,
                        "addLandscape",
                        Modifier.padding(horizontal = 10.dp),

                    )
                    Text(
                        "Ajouter des rizières",
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable {
                            scope.launch {
                                state.hide()
                                workerState.show()
                            }
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Outlined.People,
                        "addWorker",
                        Modifier.padding(horizontal = 10.dp),

                    )
                    Text(
                        "Ajouter des ouvriers",
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable { }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        //resources icone
                        Icons.Outlined.Water,
                        "addResource",
                        Modifier.padding(horizontal = 10.dp),

                    )
                    Text(
                        "Ajouter des ressources",
                    )
                }
                Divider()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable { }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Outlined.PlayArrow,
                        "start",
                        Modifier.padding(horizontal = 10.dp),

                    )
                    Text(
                        "Démarrer l'activité",
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable { }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Outlined.Check,
                        "check",
                        Modifier.padding(horizontal = 10.dp),
                    )
                    Text(
                        "Marquer comme terminée",
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable { }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        //cancel
                        Icons.Outlined.CalendarViewDay,
                        "planifier",
                        Modifier.padding(horizontal = 10.dp),
                    )
                    Text(
                        "Plannifier l'activité",
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .clickable { }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        //cancel
                        Icons.Outlined.Cancel,
                        "cancel",
                        Modifier.padding(horizontal = 10.dp),
                    )
                    Text(
                        "Annuler l'activité",
                    )
                }
                Divider()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colors.error)
                        .clickable { }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        "delete",
                        Modifier.padding(horizontal = 10.dp),
                        tint = Color.White
                    )
                    Text(
                        "Supprimer l'activité",
                        color = Color.White
                    )
                }
            }
        }
    ){


    }
    viewModel.activity?.let{
        AddPaddyFieldModalBottomSheet(
            modalBottomSheetState = paddyFieldState,
            viewModel =
                addPaddyFieldViewModel(
                    alreadyExists = it.activityPaddyFields,
                    onAddPaddyFields = { addedActivityPaddyFields ->
                        it.activityPaddyFields  += addedActivityPaddyFields
                        scope.launch {
                            paddyFieldState.hide()
                        }
                    },
                    code = it.code
                )

        )

        AddWorkersModalBottomSheet(
                    modalBottomSheetState = workerState,
                    viewModel =  addWorkerViewModel(
                        alreadyExists = it.activityWorkers,
                        onAddWorker = { addedWorkers ->
                            it.activityWorkers += addedWorkers
                            scope.launch {
                                workerState.hide()
                            }
                        },
                        code = it.code
                    )
                )
    }

}