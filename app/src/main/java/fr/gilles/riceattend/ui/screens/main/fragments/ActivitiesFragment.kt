package fr.gilles.riceattend.ui.screens.main.fragments

import android.os.Build
import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun ActivitiesFragment(
    onMenuClick: () -> Unit = {},
    scope:CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel:ActivitiesViewModel = remember {ActivitiesViewModel()}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBar(title = "Activites", leftContent = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.Menu, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = { navHostController.navigate(Route.ActivityCreationRoute.path) }) {
                Icon(Icons.Outlined.Add, "Add", tint = Color.White)
            }
        })
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.FilterList, "Filtrer")
                    Text("Filtrer", modifier = Modifier.padding(10.dp))
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            when(viewModel.loading){
                true ->{
                    LoadingCard()
                }
                false -> {
                    when(viewModel.activities){
                        null ->{
                            LoadingCard()
                        }
                        else -> {
                            viewModel.activities?.let {
                                if (it.empty == true){
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Aucune activité")
                                    }
                                }else{
                                   Column(
                                       Modifier
                                           .fillMaxSize()
                                           .verticalScroll(rememberScrollState()),
                                       verticalArrangement = Arrangement.Top,
                                       horizontalAlignment = Alignment.CenterHorizontally
                                   ) {
                                       it.content.forEach{
                                               activity -> ActivityTile()
                                       }
                                   }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


class ActivitiesViewModel:ViewModel(){
    var activities by mutableStateOf<Page<Activity>?>(null)
    var loading by mutableStateOf(false)
    val params by mutableStateOf<Params>(Params())
    val searchState by mutableStateOf(TextFieldState(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Valeur a rechercher requis"
    }, defaultValue = ""))

    init {
        loadActivities()
    }

    private fun loadActivities(){
        loading = true
        viewModelScope.launch{
            ApiEndpoint.activityRepository.getActivities(params.toMap())
                .enqueue(object: ApiCallback<Page<Activity>>(){
                    override fun onSuccess(response: Page<Activity>) {
                        activities = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("ActivitiesViewModel", error.message)
                    }

                })
        }
    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview("ActivitiesFragmentPreview", showBackground = true)
fun ActivityCreationScreen(viewModel:ActivityCreationViewModel = remember {ActivityCreationViewModel()}) {
    var currentStep by remember{ mutableStateOf(0) }
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        AppBar(title = "Creation d'une activite", leftContent = {
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.ArrowBack, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = {  }) {
            }
        })
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 20.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically) {
            when(currentStep){
                0 ->{
                    Text("1. Informations" ,Modifier.padding(horizontal = 10.dp))
                    Divider()
                }
                1->{
                    Text("2. Rizieres concernées")
                    Divider()
                }
                2 ->{
                    Text("3. Ouvriers concernés")
                }
                3 ->{
                    Text("4. Validation")
                }
            }
        }
        Column(Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
            when(currentStep){
                0 ->{

                    Text(text ="Les champs suivis de (*) sont les champs obligatoires", modifier = Modifier.padding(horizontal = 10.dp))
                    Text(text= "Clickez sur l'icone date pour choisir une date", modifier = Modifier.padding(horizontal = 10.dp))
                    InputWidget(
                        state = viewModel.activityFormViewModel.name,
                        title = "Nom de l'activité (*)",
                    )
                    InputWidget(
                        state = viewModel.activityFormViewModel.description,
                        title = "Description de l'activité",
                        singleLine = false
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape =  RoundedCornerShape(10.dp),
                        value = viewModel.activityFormViewModel.convertInstantToReadableHumanDate(
                            viewModel.activityFormViewModel.startDate.value
                        ) ,
                        onValueChange = {

                        },
                        label = { Text("Date de début (*)" )},
                        trailingIcon = {
                            IconButton(onClick = {}){
                                Icon(Icons.Outlined.CalendarToday, "Calendrier")
                            }
                        }
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape =  RoundedCornerShape(10.dp),
                        value = viewModel.activityFormViewModel.convertInstantToReadableHumanDate(
                            viewModel.activityFormViewModel.startDate.value
                        ) ,
                        onValueChange = {

                        },
                        label = { Text("Date de fin (*)" )},
                        trailingIcon = {
                            IconButton(onClick = {}){
                                Icon(Icons.Outlined.CalendarToday, "Calendrier")
                            }
                        }
                    )
                }
                1->{
                    Text(text = "Selectionner les rizieres concernées en cliquant sur la case a cocher",
                        modifier = Modifier.padding(horizontal = 10.dp))

                    when(viewModel.paddyFields){
                        null ->{
                            LoadingCard()
                        }
                        else ->{
                            viewModel.paddyFields?.let {
                                if(it.empty == true)
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Pas de rizieres")
                                    }
                                else
                                    Column(Modifier.fillMaxSize()){
                                        it.content.forEach {
                                                paddyField ->
                                            Card(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 5.dp)) {
                                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                    Checkbox(checked = viewModel.activityFormViewModel.paddyFields.value.contains(paddyField), onCheckedChange ={ checked ->
                                                        if(checked)  viewModel.activityFormViewModel.paddyFields.let{ paddyFields ->
                                                            paddyFields.value = paddyFields.value + paddyField
                                                        }
                                                        else  viewModel.activityFormViewModel.paddyFields.let{ paddyFields ->
                                                            paddyFields.value = paddyFields.value - paddyField
                                                        }
                                                    })
                                                    PaddyFieldTile(paddyField = paddyField)
                                                }
                                            }
                                        }
                                    }

                            }
                        }
                    }
                }
                2->{
                    Text(text = "Selectionner les ouvriers concernées en cliquant sur la case a cocher",
                        modifier = Modifier.padding(horizontal = 10.dp))

                    when(viewModel.paddyFields){
                        null ->{
                            LoadingCard()
                        }
                        else ->{
                            viewModel.workers?.let {
                                if(it.empty == true)
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Pas d'ouvriers")
                                    }
                                else
                                    Column(Modifier.fillMaxSize()){
                                        it.content.forEach {
                                                worker ->
                                            Card(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 5.dp)) {
                                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                    Checkbox(checked = viewModel.activityFormViewModel.workers.value.contains(worker), onCheckedChange ={ checked ->
                                                        if(checked) viewModel.activityFormViewModel.workers.let{ workers ->
                                                            workers.value = workers.value + worker
                                                        }
                                                        else viewModel.activityFormViewModel.workers.let{ workers ->
                                                            workers.value = workers.value - worker
                                                        }
                                                    })
                                                    WorkerTile(worker = worker)
                                                }
                                            }
                                        }
                                    }

                            }
                        }
                    }

                }
                3->{

                }
                4 ->{

                }
                else ->{

                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            if(currentStep > 0){
                Button(onClick = { currentStep-- },
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ArrowBack, "Retour")
                        Text("Précédent", modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }
            }
            if(currentStep < 4){
                Button(onClick = { currentStep++ },
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Suivant", modifier = Modifier.padding(horizontal = 10.dp))
                        Icon(Icons.Outlined.ArrowForward, "Suivant")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class ActivityCreationViewModel:ViewModel(){
    val activityFormViewModel by mutableStateOf(ActivityFormViewModel())
    var paddyFields by mutableStateOf<Page<PaddyField>?>(null)
    var workers by mutableStateOf<Page<Worker>?>(null)


    init {
        loadPaddyField()
        loadWorkers()
    }

    fun loadPaddyField(){
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.get(Params(0,100).toMap())
                .enqueue(object: ApiCallback<Page<PaddyField>>(){
                    override fun onSuccess(response: Page<PaddyField>) {
                        paddyFields = response
                    }

                    override fun onError(error: ApiResponseError) {
                        
                    }
                })
        }
    }
    fun loadWorkers(){
        viewModelScope.launch {
            ApiEndpoint.workerRepository.get(Params(0,100).toMap())
                .enqueue(object: ApiCallback<Page<Worker>>(){
                    override fun onSuccess(response: Page<Worker>) {
                        workers = response
                    }

                    override fun onError(error: ApiResponseError) {

                    }
                })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class ActivityFormViewModel{
    var name by mutableStateOf(TextFieldState(
        defaultValue = "",
        validator = {
            it.isNotBlank() && it.isNotEmpty()
        },
        errorMessage = {
            "Nom requis"
        }
    ))
    var description by mutableStateOf(TextFieldState(
        defaultValue = "",
        validator = {
            true
        },
        errorMessage = {
            "Description requise"
        }
    ))

    var startDate by mutableStateOf(TextFieldState<Instant>(
        defaultValue = Instant.now(),
        validator = {
            it.isAfter(Instant.now())
        },
        errorMessage = {
            "Date de début requise"
        }
    ))

    var endDate by mutableStateOf(TextFieldState<Instant>(
        defaultValue = Instant.now(),
        validator = {
            it.isAfter(startDate.value)
        },
        errorMessage = {
            "Date de fin requise"
        }
    ))
    var paddyFields by mutableStateOf(TextFieldState<List<PaddyField>>(
        defaultValue = listOf(),
        validator = {
            it.isNotEmpty()
        },
        errorMessage = {
            "Veuillez selectionner au moins une riziere"
        }
    ))

    var workers by mutableStateOf(TextFieldState<List<Worker>>(
        defaultValue = listOf(),
        validator = {
            true
        },
        errorMessage = {
            ""
        }
    ))


    fun convertInstantToReadableHumanDate(instant: Instant):String{
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.FRANCE)
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }
}

