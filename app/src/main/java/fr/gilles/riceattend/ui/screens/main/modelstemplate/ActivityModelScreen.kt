package fr.gilles.riceattend.ui.screens.main.modelstemplate

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch
import java.time.Duration

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityModelScreen(
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: ActivityViewModel
){
    val scope = rememberCoroutineScope()
    when (viewModel.loading) {
        true -> LoadingCard()
        false -> {
            when (viewModel.activity) {
                null -> {}
                else -> {
                    viewModel.activity?.let {
                        val addPaddyFieldModalBottomSheetState = rememberModalBottomSheetState(
                            initialValue = ModalBottomSheetValue.Hidden
                        )
                        val addWorkersModalBottomSheetState = rememberModalBottomSheetState(
                            initialValue = ModalBottomSheetValue.Hidden
                        )
                        Column(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.primary)
                                    .padding(bottom = 15.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier =Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Row(
                                        modifier = Modifier
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = onMenuClick) {
                                            Icon(
                                                Icons.Outlined.ArrowBack,
                                                "Back ",
                                                tint = MaterialTheme.colors.background
                                            )
                                        }
                                        Text(
                                            text = it.name,
                                            style = MaterialTheme.typography.h1,
                                            color = MaterialTheme.colors.background,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }

                                   IconButton(onClick = {  }, modifier =Modifier.padding(end = 12.dp)) {
                                       Icon(
                                           Icons.Outlined.MoreVert,
                                           "View More",
                                           tint = MaterialTheme.colors.background
                                       )
                                   }
                                    //actions
                                    val actions = remember {
                                        mutableListOf(
                                            mapOf(
                                                "title" to "Modifier",
                                                "icon" to Icons.Outlined.Edit,
                                                "action" to {

                                                }
                                            ),
                                             mapOf(
                                                 "title" to "Marquer comme debute",
                                                 "icon" to Icons.Outlined.PlayArrow,
                                                 "action" to {
                                                     viewModel.maskAsStarted()
                                                 }
                                             ),
                                            mapOf(
                                                "title" to "Marquer comme fait",
                                                "icon" to Icons.Outlined.Done,
                                                "action" to {
                                                    viewModel.markAsDone()
                                                }


                                            ),
                                            mapOf(
                                                "title" to "Annuler",
                                                "icon" to Icons.Outlined.Cancel,
                                                "action" to {
                                                    viewModel.cancelActivity()
                                                }
                                            ),
                                            mapOf(
                                                "title" to "Supprimer",
                                                "icon" to Icons.Outlined.Delete,
                                                "action" to {
                                                    viewModel.deleteActivity()
                                                }
                                            )
                                        )
                                    }
                                }

                                Card(
                                    modifier = Modifier
                                        .padding(bottom = 20.dp)
                                        .size(200.dp)
                                        .clip(CircleShape),
                                    elevation = 8.dp
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(Icons.Outlined.Timelapse, "Timelapse")
                                        Text(text = "Duree Totale")
                                        Text(
                                            text = (Duration.between(
                                                it.startDate.toInstant(),
                                                it.endDate.toInstant()
                                            ).seconds / (3600 * 24)).toInt().toString() + " Jours",
                                            style = MaterialTheme.typography.h1,
                                            fontSize = 20.sp,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                        Badge(
                                            backgroundColor = MaterialTheme.colors.primary,
                                            contentColor = MaterialTheme.colors.background,
                                            modifier = Modifier.clip(CircleShape),
                                        ) {
                                            Text(
                                                text = it.status.value,
                                                Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                                                style = MaterialTheme.typography.body1,
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 15.dp, end = 15.dp),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Debut",
                                            color = MaterialTheme.colors.background
                                        )
                                        Text(
                                            text = formatDateToHumanReadable(it.startDate),
                                            color = MaterialTheme.colors.background
                                        )
                                    }
                                    Divider(
                                        color = MaterialTheme.colors.background,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Column {
                                        Text(text = "Fin", color = MaterialTheme.colors.background)
                                        Text(
                                            text = formatDateToHumanReadable(it.endDate),
                                            color = MaterialTheme.colors.background
                                        )
                                    }

                                }

                            }
                            var selectedIndex by remember {
                                mutableStateOf(0)
                            }
                            val tabs = listOf(
                                "Rizieres (${viewModel.activityPaddyFields.size})",
                                "Ouvriers (${viewModel.activityWorkers.size})",
                                "Ressources (${viewModel.activityResources.size})"
                            )
                            TabRow(
                                selectedTabIndex = selectedIndex,
                                backgroundColor = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomEnd = 15.dp,
                                            bottomStart = 15.dp
                                        )
                                    )
                            ) {
                                tabs.forEachIndexed { index, tab ->
                                    Tab(
                                        selected = selectedIndex == index,
                                        onClick = { selectedIndex = index },
                                    ) {
                                        Text(
                                            text = tab,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                when (selectedIndex) {
                                    0 -> {
                                        var selectionMode by remember{ mutableStateOf(false) }
                                       LazyColumn(Modifier.fillMaxWidth()){
                                           stickyHeader {
                                               Row(
                                                   Modifier
                                                       .fillMaxWidth()
                                                       .padding(
                                                           horizontal = 12.dp,
                                                           vertical = 10.dp
                                                       ).background(MaterialTheme.colors.background),
                                                   verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                               ){
                                                   if(selectionMode){

                                                       Row(verticalAlignment = Alignment.CenterVertically,
                                                           modifier = Modifier.clickable{
                                                               if(viewModel.selectedActivityPaddyFields.isEmpty()){
                                                                   viewModel.selectedActivityPaddyFields = viewModel.activityPaddyFields
                                                               }else {
                                                                   viewModel.selectedActivityPaddyFields = listOf()
                                                               }
                                                           }
                                                       ){
                                                           if (viewModel.selectedActivityPaddyFields.isEmpty()){
                                                               Icon(
                                                                   Icons.Outlined.Check,
                                                                   "Tout selectionner",
                                                                   modifier =Modifier.size(18.dp)
                                                               )
                                                               Text(
                                                                   text = "Tout selectionner",
                                                                   modifier = Modifier.padding(start = 5.dp),
                                                                   fontSize = 12.sp
                                                               )
                                                           }else{
                                                               Icon(
                                                                   Icons.Outlined.CheckBox,
                                                                   "Tout selectionner",
                                                                   modifier =Modifier.size(18.dp)
                                                               )
                                                               Text(
                                                                   text = "Tout deselectionner",
                                                                   modifier = Modifier.padding(start = 5.dp),
                                                                   fontSize = 12.sp
                                                               )
                                                           }

                                                       }
                                                       Row(verticalAlignment = Alignment.CenterVertically){
                                                           Icon(
                                                               Icons.Outlined.RemoveFromQueue,
                                                               "remove",
                                                               modifier =Modifier.size(18.dp))
                                                           Text(
                                                               text = "Retirer la selection",
                                                               modifier = Modifier.padding(start = 5.dp),
                                                               fontSize = 12.sp
                                                           )
                                                       }

                                                       Row(verticalAlignment = Alignment.CenterVertically,
                                                           modifier = Modifier.clickable{
                                                               selectionMode = false
                                                              viewModel.selectedActivityPaddyFields = listOf()
                                                       }){
                                                           Icon(Icons.Outlined.Cancel, "cancel",modifier =Modifier.size(18.dp))
                                                           Text("Annuler",
                                                               Modifier
                                                                   .clickable {
                                                                       selectionMode = false
                                                                   }
                                                                   .padding(start = 5.dp),fontSize = 12.sp)
                                                       }

                                                   }else{
                                                       Row( verticalAlignment = Alignment.CenterVertically,
                                                           horizontalArrangement = Arrangement.SpaceBetween,
                                                           modifier = Modifier
                                                               .clickable {
                                                                   viewModel.activityPaddyFields
                                                                       .sortedBy {
                                                                           it.paddyField.name
                                                                       }
                                                                       .also {
                                                                           viewModel.activityPaddyFields =
                                                                               it
                                                                       }
                                                               }
                                                               .clip(RoundedCornerShape(15.dp))
                                                               .padding(
                                                                   start = 15.dp,
                                                                   end = 15.dp,
                                                                   top = 5.dp,
                                                                   bottom = 5.dp
                                                               )

                                                       ){
                                                           Icon(Icons.Outlined.FilterList, "Filter")
                                                           Text(text = "Filtrer",fontSize = 12.sp)
                                                       }
                                                       IconButton(onClick = { scope.launch {
                                                           addPaddyFieldModalBottomSheetState.show()
                                                       } }) {
                                                           Icon(
                                                               Icons.Outlined.Add,
                                                               "Ajouter"
                                                           )
                                                       }
                                                   }

                                               }
                                           }
                                            items(viewModel.activityPaddyFields.size){index ->
                                                Card(Modifier.fillMaxWidth()
                                                ) {
                                                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                                        if(selectionMode)
                                                        Checkbox(
                                                            checked = viewModel.selectedActivityPaddyFields.contains(viewModel.activityPaddyFields[index]) ,
                                                            onCheckedChange = {
                                                                if(it){
                                                                    if(!viewModel.selectedActivityPaddyFields.contains(viewModel.activityPaddyFields[index])){
                                                                        viewModel.selectedActivityPaddyFields += viewModel.activityPaddyFields[index]
                                                                    }
                                                                }else{
                                                                    if(viewModel.selectedActivityPaddyFields.contains(viewModel.activityPaddyFields[index])){
                                                                        viewModel.selectedActivityPaddyFields -= viewModel.activityPaddyFields[index]
                                                                    }
                                                                }
                                                            })

                                                        PaddyFieldTile(
                                                            paddyField = viewModel.activityPaddyFields[index].paddyField,
                                                            onLongClick = {
                                                               selectionMode = !selectionMode
                                                                viewModel.selectedActivityPaddyFields = listOf()
                                                            },
                                                            badgeContent = {
                                                                Badge(
                                                                    backgroundColor = MaterialTheme.colors.primary,
                                                                    contentColor = MaterialTheme.colors.background,
                                                                    modifier = Modifier.clip(CircleShape),
                                                                ) {
                                                                    Text(
                                                                        text = viewModel.activityPaddyFields[index].status.value,
                                                                        Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                                                                        style = MaterialTheme.typography.body1,
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    }

                                                }
                                            }
                                       }
                                    }
                                    1 -> {
                                        var selectionMode by remember{ mutableStateOf(false) }
                                        LazyColumn(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)){
                                            stickyHeader {
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            horizontal = 12.dp,
                                                            vertical = 10.dp
                                                        ).background(MaterialTheme.colors.background),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                ){
                                                    if(selectionMode){

                                                        Row(verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier.clickable{
                                                                if(viewModel.selectedActivityWorkers.isEmpty()){
                                                                    viewModel.selectedActivityWorkers = viewModel.activityWorkers
                                                                }else {
                                                                    viewModel.selectedActivityWorkers = listOf()
                                                                }
                                                            }
                                                        ){
                                                            if (viewModel.selectedActivityWorkers.isEmpty()){
                                                                Icon(
                                                                    Icons.Outlined.Check,
                                                                    "Tout selectionner",
                                                                    modifier =Modifier.size(18.dp)
                                                                )
                                                                Text(
                                                                    text = "Tout selectionner",
                                                                    modifier = Modifier.padding(start = 5.dp),
                                                                    fontSize = 12.sp
                                                                )
                                                            }else{
                                                                Icon(
                                                                    Icons.Outlined.CheckBox,
                                                                    "Tout selectionner",
                                                                    modifier =Modifier.size(18.dp)
                                                                )
                                                                Text(
                                                                    text = "Tout deselectionner",
                                                                    modifier = Modifier.padding(start = 5.dp),
                                                                    fontSize = 12.sp
                                                                )
                                                            }

                                                        }
                                                        Row(verticalAlignment = Alignment.CenterVertically){
                                                            Icon(
                                                                Icons.Outlined.RemoveFromQueue,
                                                                "remove",
                                                                modifier =Modifier.size(18.dp))
                                                            Text(
                                                                text = "Retirer la selection",
                                                                modifier = Modifier.padding(start = 5.dp),
                                                                fontSize = 12.sp
                                                            )
                                                        }

                                                        Row(verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier.clickable{
                                                                selectionMode = false
                                                                viewModel.selectedActivityWorkers = listOf()
                                                            }){
                                                            Icon(Icons.Outlined.Cancel, "cancel",modifier =Modifier.size(18.dp))
                                                            Text("Annuler",
                                                                Modifier
                                                                    .clickable {
                                                                        selectionMode = false
                                                                    }
                                                                    .padding(start = 5.dp),fontSize = 12.sp)
                                                        }

                                                    }else{
                                                        Row( verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            modifier = Modifier
                                                                .clickable {
                                                                    viewModel.selectedActivityWorkers.sortedBy {
                                                                        it.worker.name
                                                                    }
                                                                }
                                                                .clip(RoundedCornerShape(15.dp))
                                                                .padding(
                                                                    start = 15.dp,
                                                                    end = 15.dp,
                                                                    top = 5.dp,
                                                                    bottom = 5.dp
                                                                )

                                                        ){
                                                            Icon(Icons.Outlined.FilterList, "Filter")
                                                            Text(text = "Filtrer",fontSize = 12.sp)
                                                        }
                                                        IconButton(onClick = { scope.launch{addWorkersModalBottomSheetState.show()} }) {
                                                            Icon(
                                                                Icons.Outlined.Add,
                                                                "Ajouter"
                                                            )
                                                        }
                                                    }

                                                }
                                            }
                                            items(viewModel.activityWorkers.size){index ->
                                                Card(Modifier
                                                        .fillMaxWidth()) {
                                                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                        if (selectionMode)
                                                            Checkbox(
                                                                checked = viewModel.selectedActivityWorkers.contains(
                                                                    viewModel.activityWorkers[index]
                                                                ),
                                                                onCheckedChange = {
                                                                    if (it) {
                                                                        if (!viewModel.selectedActivityWorkers.contains(
                                                                                viewModel.activityWorkers[index]
                                                                            )
                                                                        ) {
                                                                            viewModel.selectedActivityWorkers += viewModel.activityWorkers[index]
                                                                        }
                                                                    } else {
                                                                        if (viewModel.selectedActivityWorkers.contains(
                                                                                viewModel.activityWorkers[index]
                                                                            )
                                                                        ) {
                                                                            viewModel.selectedActivityWorkers -= viewModel.activityWorkers[index]
                                                                        }
                                                                    }
                                                                })
                                                        WorkerTile(
                                                            worker = viewModel.activityWorkers[index].worker,
                                                            onLongClick = {
                                                                selectionMode = !selectionMode
                                                                viewModel.selectedActivityWorkers = listOf()
                                                            }
                                                        )
                                                    }

                                                }
                                            }
                                        }
                                        
                                    }
                                    2 ->{
                                        LazyColumn(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)){
                                            items(viewModel.activityResources.size){index ->
                                                Card(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable { }) {
                                                    Column(Modifier.fillMaxWidth()){
                                                        ResourceTile(
                                                            resource = viewModel.activityResources[index].resource,
                                                        )
                                                        Row(
                                                            Modifier
                                                                .fillMaxWidth()
                                                                .padding(
                                                                    bottom = 10.dp,
                                                                    start = 10.dp,
                                                                    end = 10.dp
                                                                ),
                                                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                                            Text("Quantite utilisee : ${viewModel.activityResources[index].quantity}",
                                                                style = MaterialTheme.typography.body1,
                                                                modifier = Modifier.padding(start = 20.dp),
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                            Text("Cout : ${viewModel.activityResources[index].value} FCFA",
                                                                style = MaterialTheme.typography.body1,
                                                                modifier = Modifier.padding(start = 20.dp),
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        AddPaddyFieldModalBottomSheet(
                            modalBottomSheetState = addPaddyFieldModalBottomSheetState,
                            viewModel =  remember {AddPaddyFieldViewModel(
                                alreadyExists = viewModel.activityPaddyFields,
                                onAddPaddyFields = { addedActivityPaddyFields ->
                                    viewModel.activityPaddyFields += addedActivityPaddyFields
                                    scope.launch {
                                        addPaddyFieldModalBottomSheetState.hide()
                                    }
                                },
                                activityCode = it.code)
                            }
                        )
                        AddWorkersModalBottomSheet(
                            modalBottomSheetState = addWorkersModalBottomSheetState,
                            viewModel =  remember {AddWorkersViewModel(
                                alreadyExists = viewModel.activityWorkers,
                                onAddWorkersToActivity = { addedWorkers ->
                                    viewModel.activityWorkers += addedWorkers
                                    scope.launch {
                                        addWorkersModalBottomSheetState.hide()
                                    }
                                },
                                activityCode = it.code)
                            }
                        )
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
class ActivityViewModel(val code: String) : ViewModel() {
    var activity by mutableStateOf<Activity?>(null)
    var loading by mutableStateOf(false)
    var activityResources by mutableStateOf<List<ActivityResource>>(listOf())
    var activityWorkers by mutableStateOf<List<ActivityWorker>>(listOf())
    var activityPaddyFields by mutableStateOf<List<ActivityPaddyField>>(listOf())

    var selectedActivityResources by mutableStateOf<List<ActivityResource>>(listOf())
    var selectedActivityWorkers by mutableStateOf<List<ActivityWorker>>(listOf())
    var selectedActivityPaddyFields by mutableStateOf<List<ActivityPaddyField>>(listOf())

    init {
        loadActivity()
    }


    private fun loadActivity() {
        loading = true
        Log.d("Launch one time scope", "1 launch")
        viewModelScope.launch {
            ApiEndpoint.activityRepository.get(code)
                .enqueue(object : ApiCallback<Activity>() {
                    override fun onSuccess(response: Activity) {
                        activity = response
                        getActivityPaddyFields()
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }

                })
        }
    }
    
    private fun getActivityResources(){
        activity?.let {
            viewModelScope.launch {
                ApiEndpoint.activityRepository.getActivityResources(it.code)
                    .enqueue(object : ApiCallback<List<ActivityResource>>() {
                        override fun onSuccess(response: List<ActivityResource>) {
                            activityResources = response
                            getActivityWorkers()
                        }

                        override fun onError(error: ApiResponseError) {
                        }

                    })
            }
        }
        
    }
    
    private fun getActivityWorkers(){
        activity?.let {
            viewModelScope.launch {
                ApiEndpoint.activityRepository.getActivityWorkers(it.code)
                    .enqueue(object : ApiCallback<List<ActivityWorker>>() {
                        override fun onSuccess(response: List<ActivityWorker>) {
                            activityWorkers = response
                            loading = false
                        }

                        override fun onError(error: ApiResponseError) {
                        }

                    })
            }
        }
        
    }

    fun addPaddyFieldCurrentActivity(paddyFields:List<PaddyField>){

    }
    
    private fun getActivityPaddyFields(){
        activity?.let {
            viewModelScope.launch {
                ApiEndpoint.activityRepository.getActivityPaddyFields(it.code)
                    .enqueue(object : ApiCallback<List<ActivityPaddyField>>() {
                        override fun onSuccess(response: List<ActivityPaddyField>) {
                            activityPaddyFields = response
                            getActivityResources()
                        }

                        override fun onError(error: ApiResponseError) {
                        }

                    })
            }
        }
        
    }

    fun markAsDone(){

    }

    fun markAsUnDone(){

    }

    fun maskAsStarted(){

    }


    fun cancelActivity(){

    }

    fun deleteActivity(){

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Swipe(content: @Composable () -> Unit) {
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default
    )
    SwipeToDismiss(
        state = dismissState,
        background = {
            val color = Color.Transparent
            val direction = dismissState.dismissDirection
            if (direction == DismissDirection.EndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.heightIn(5.dp))
                        Text(
                            text = "Retirer",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )

                    }
                }
            }
        },
        /**** Dismiss Content */
        dismissContent = {
            content()
        },
        /*** Set Direction to dismiss */
        directions = setOf(DismissDirection.EndToStart),
    )
}
