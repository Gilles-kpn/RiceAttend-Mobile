package fr.gilles.riceattend.ui.screens.main.fragments

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fr.gilles.riceattend.R
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.ErrorText
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivitiesFragment(
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController,
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState,

    viewModel: ActivitiesViewModel = remember { ActivitiesViewModel() }
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
                    when (viewModel.activities) {
                        null -> {
                            LoadingCard()
                        }
                        else -> {
                            viewModel.activities?.let {
                                if (it.empty == true) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Aucune activité")
                                    }
                                } else {
                                    Column(
                                        Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState()),
                                        verticalArrangement = Arrangement.Top,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        it.content.forEach { activity ->
                                            ActivityTile(activity = activity, onClick = {
                                                navHostController.navigate(
                                                    Route.ActivityRoute.path.replace(
                                                        "{code}",
                                                        activity.code
                                                    )
                                                ){
                                                }
                                            })
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

@RequiresApi(Build.VERSION_CODES.O)
class ActivitiesViewModel : ViewModel() {
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


    private fun loadActivities() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.activityRepository.getActivities(params.toMap())
                .enqueue(object : ApiCallback<Page<Activity>>() {
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
fun ActivityCreationScreen(
    viewModel: ActivityCreationViewModel = remember { ActivityCreationViewModel() },
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController
) {
    var currentStep by remember { mutableStateOf(0) }
    var currentStepHasError by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(title = "Creation d'une activite", leftContent = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.ArrowBack, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = { }) {}
        })
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 20.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (currentStep) {
                0 -> {
                    Text("1. Informations", Modifier.padding(horizontal = 10.dp))
                    Divider()
                }
                1 -> {
                    Text("2. Rizieres concernées")
                    Divider()
                }
                2 -> {
                    Text("3. Ouvriers concernés")
                }
                3 -> {
                    Text("4. Validation")
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            if (currentStepHasError)
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    backgroundColor = Color.Red.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IncludeLottieFile(
                                draw = R.raw.error, modifier = Modifier
                                    .size(25.dp, 25.dp)
                                    .padding(start = 10.dp)
                            )
                            Text(
                                text = viewModel.activityFormViewModel.errorsStepMessage[currentStep](),
                                modifier = Modifier.padding(horizontal = 10.dp),
                                color = MaterialTheme.colors.contentColorFor(Color.Red.copy(alpha = 0.5f))
                            )
                        }

                        IconButton(onClick = { currentStepHasError = false }) {
                            Icon(Icons.Default.Close, "Close error")
                        }
                    }
                }
            when (currentStep) {
                0 -> {
                    var dateSelectListener by remember { mutableStateOf<(Instant) -> Unit>({}) }
                    var showDateDialog by remember {
                        mutableStateOf(false)
                    }
                    if (showDateDialog) ShowDatePicker(onDateSelected = dateSelectListener)

                    Text(
                        text = "Les champs suivis de (*) sont les champs obligatoires",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Text(
                        text = "Clickez sur l'icone date pour choisir une date",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
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
                        shape = RoundedCornerShape(10.dp),
                        value = viewModel.activityFormViewModel.convertInstantToReadableHumanDate(
                            viewModel.activityFormViewModel.startDate.value
                        ),
                        onValueChange = {
                            viewModel.activityFormViewModel.endDate.validate()
                        },
                        label = { Text("Date de début (*)") },
                        trailingIcon = {
                            IconButton(onClick = {
                                dateSelectListener = {
                                    viewModel.activityFormViewModel.startDate.value = it
                                    viewModel.activityFormViewModel.startDate.validate()
                                    showDateDialog = false
                                }
                                showDateDialog = true
                            }) {
                                Icon(Icons.Outlined.CalendarToday, "Calendrier")
                            }
                        },
                        isError = viewModel.activityFormViewModel.startDate.error != null,
                    )
                    viewModel.activityFormViewModel.startDate.error?.let {
                        ErrorText(text = it)
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        value = viewModel.activityFormViewModel.convertInstantToReadableHumanDate(
                            viewModel.activityFormViewModel.endDate.value
                        ),
                        onValueChange = {
                            viewModel.activityFormViewModel.endDate.validate()
                        },
                        label = { Text("Date de fin (*)") },
                        trailingIcon = {
                            IconButton(onClick = {
                                dateSelectListener = {
                                    viewModel.activityFormViewModel.endDate.value = it
                                    viewModel.activityFormViewModel.endDate.validate()
                                    showDateDialog = false
                                }
                                showDateDialog = true
                            }) {
                                Icon(Icons.Outlined.CalendarToday, "Calendrier")
                            }
                        },
                        isError = viewModel.activityFormViewModel.endDate.error != null,
                    )

                    viewModel.activityFormViewModel.endDate.error?.let {
                        ErrorText(text = it)
                    }
                }
                1 -> {
                    Text(
                        text = "Selectionner les rizieres concernées en cliquant sur la case a cocher",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    when (viewModel.paddyFields) {
                        null -> {
                            LoadingCard()
                        }
                        else -> {
                            viewModel.paddyFields?.let {
                                if (it.empty == true)
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Pas de rizieres")
                                    }
                                else
                                    Column(Modifier.fillMaxSize()) {
                                        it.content.forEach { paddyField ->
                                            Card(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 5.dp)
                                            ) {
                                                Row(
                                                    Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Checkbox(
                                                        checked = viewModel.activityFormViewModel.paddyFields.value.contains(
                                                            paddyField
                                                        ), onCheckedChange = { checked ->
                                                            if (checked) viewModel.activityFormViewModel.paddyFields.let { paddyFields ->
                                                                paddyFields.value =
                                                                    paddyFields.value + paddyField
                                                            }
                                                            else viewModel.activityFormViewModel.paddyFields.let { paddyFields ->
                                                                paddyFields.value =
                                                                    paddyFields.value - paddyField
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
                2 -> {
                    Text(
                        text = "Selectionner les ouvriers concernées en cliquant sur la case a cocher",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    when (viewModel.paddyFields) {
                        null -> {
                            LoadingCard()
                        }
                        else -> {
                            viewModel.workers?.let {
                                if (it.empty == true)
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Pas d'ouvriers")
                                    }
                                else
                                    Column(Modifier.fillMaxSize()) {
                                        it.content.forEach { worker ->
                                            Card(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 5.dp)
                                            ) {
                                                Row(
                                                    Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Checkbox(
                                                        checked = viewModel.activityFormViewModel.workers.value.contains(
                                                            worker
                                                        ), onCheckedChange = { checked ->
                                                            if (checked) viewModel.activityFormViewModel.workers.let { workers ->
                                                                workers.value =
                                                                    workers.value + worker
                                                            }
                                                            else viewModel.activityFormViewModel.workers.let { workers ->
                                                                workers.value =
                                                                    workers.value - worker
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
                3 -> {
                    Text(
                        text = "Selectionner les ressources a utiliser en cliquant sur la case a cocher",
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    when (viewModel.resources) {
                        null -> {
                            LoadingCard()
                        }
                        else -> {
                            viewModel.resources?.let {
                                if (it.empty == true)
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Pas de ressources")
                                    }
                                else
                                    Column(Modifier.fillMaxSize()) {
                                        it.content.forEach { resource ->
                                            Card(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 5.dp)
                                            ) {
                                                var isChecked by remember { mutableStateOf(false) }
                                                isChecked =
                                                    viewModel.activityFormViewModel.activityResources.value
                                                        .stream().anyMatch { activityResource ->
                                                            activityResource.resourceCode == resource.code
                                                        }
                                                Column {
                                                    Row(
                                                        Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Checkbox(
                                                            checked = viewModel.activityFormViewModel.activityResources.value
                                                                .stream()
                                                                .anyMatch { activityResource ->
                                                                    activityResource.resourceCode == resource.code
                                                                },
                                                            onCheckedChange = { checked ->
                                                                if (checked) viewModel.activityFormViewModel.activityResources.let { activityResource ->
                                                                    activityResource.value =
                                                                        activityResource.value + ActivityResourcePayload(
                                                                            resourceCode = resource.code,
                                                                            quantity = 1f
                                                                        )
                                                                }
                                                                else viewModel.activityFormViewModel.activityResources.let { activityResource ->
                                                                    activityResource.value =
                                                                        activityResource.value.filter { activityResourcePayload ->
                                                                            activityResourcePayload.resourceCode == resource.code
                                                                        }
                                                                }
                                                                isChecked = checked
                                                            })
                                                        ResourceTile(resource = resource)
                                                    }
                                                    if (isChecked) {
                                                        val quantityState by remember {
                                                            mutableStateOf(TextFieldState<Int>(
                                                                defaultValue = viewModel.activityFormViewModel.activityResources.value.stream()
                                                                    .filter { activityResource ->
                                                                        activityResource.resourceCode == resource.code
                                                                    }.map { activityResource ->
                                                                        activityResource.quantity
                                                                    }.findFirst().orElse(0f)
                                                                    .toInt(),
                                                                validator = { quantity -> quantity > 0 && quantity <= resource.quantity },
                                                                errorMessage = { "La quantité doit être comprise entre 1 et ${resource.quantity}" },

                                                                )
                                                            )
                                                        }
                                                        InputNumberWidget(
                                                            state = quantityState,
                                                            title = "Quantite de ${resource.name} a utiliser",
                                                            onChange = { quantity ->
                                                                viewModel.activityFormViewModel.activityResources.let { activityResource ->
                                                                    activityResource.value =
                                                                        activityResource.value.map { activityResourcePayload ->
                                                                            if (activityResourcePayload.resourceCode == resource.code)
                                                                                activityResourcePayload.copy(
                                                                                    quantity = quantity.toFloat()
                                                                                )
                                                                            else
                                                                                activityResourcePayload
                                                                        }
                                                                }
                                                            })
                                                    }
                                                }
                                            }
                                        }
                                    }
                            }
                        }
                    }
                }
                4 -> {
                    when (viewModel.loading) {
                        true -> LoadingCard()
                        false -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                when (viewModel.createdActivity) {
                                    null -> {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            elevation = 10.dp,
                                            backgroundColor = MaterialTheme.colors.onError
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Erreur lors de la création de l'activité",
                                                    modifier = Modifier.padding(10.dp),
                                                )
                                                Button(
                                                    onClick = { currentStep = 0 },
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            vertical = 5.dp,
                                                            horizontal = 12.dp
                                                        )
                                                        .clip(CircleShape)
                                                ) {
                                                    Text(
                                                        text = "Veuillez réessayer",
                                                        modifier = Modifier.padding(10.dp),
                                                    )
                                                }
                                            }

                                        }
                                    }
                                    else -> {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp),
                                            elevation = 10.dp,
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                IncludeLottieFile(
                                                    draw = R.raw.successful,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(200.dp)
                                                )
                                                Text(
                                                    text = "Activité créée avec succès",
                                                    modifier = Modifier.padding(10.dp),
                                                    color = MaterialTheme.colors.contentColorFor(
                                                        MaterialTheme.colors.onSurface
                                                    )
                                                )
                                                Button(
                                                    onClick = {
                                                        navHostController.navigate(
                                                            Route.ActivityRoute.path.replace(
                                                                "{code}",
                                                                viewModel.createdActivity!!.code
                                                            )
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            vertical = 5.dp,
                                                            horizontal = 12.dp
                                                        )
                                                        .clip(RoundedCornerShape(15.dp))
                                                ) {
                                                    Text(text = "Voir l'activité")
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
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentStep in 1..3) {
                Button(
                    onClick = { currentStep-- },
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.ArrowBack, "Retour")
                        Text("Précédent", modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }
            }
            if (currentStep < 4) {
                Button(
                    onClick = {
                        if (viewModel.activityFormViewModel.stepsValidator[currentStep]()) {
                            currentStepHasError = false
                            if (currentStep == 3) viewModel.createActivity()
                            currentStep++
                        } else currentStepHasError = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (currentStep != 3)
                            Text("Suivant", modifier = Modifier.padding(horizontal = 10.dp))
                        else Text("Créer", modifier = Modifier.padding(horizontal = 10.dp))
                        Icon(Icons.Outlined.ArrowForward, "Suivant")
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
class ActivityCreationViewModel : ViewModel() {
    val activityFormViewModel by mutableStateOf(ActivityFormViewModel())
    var paddyFields by mutableStateOf<Page<PaddyField>?>(null)
    var workers by mutableStateOf<Page<Worker>?>(null)
    var resources by mutableStateOf<Page<Resource>?>(null)
    var loading by mutableStateOf(false)
    var createdActivity by mutableStateOf<Activity?>(null)


    init {
        loadPaddyField()
        loadWorkers()
        loadResources()
    }

    fun loadPaddyField() {
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.get(Params(0, 100).toMap())
                .enqueue(object : ApiCallback<Page<PaddyField>>() {
                    override fun onSuccess(response: Page<PaddyField>) {
                        paddyFields = response
                    }

                    override fun onError(error: ApiResponseError) {

                    }
                })
        }
    }

    fun loadWorkers() {
        viewModelScope.launch {
            ApiEndpoint.workerRepository.get(Params(0, 100).toMap())
                .enqueue(object : ApiCallback<Page<Worker>>() {
                    override fun onSuccess(response: Page<Worker>) {
                        workers = response
                    }

                    override fun onError(error: ApiResponseError) {

                    }
                })
        }
    }

    fun loadResources() {
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.get(Params(0, 100).toMap())
                .enqueue(object : ApiCallback<Page<Resource>>() {
                    override fun onSuccess(response: Page<Resource>) {
                        resources = response
                    }

                    override fun onError(error: ApiResponseError) {
                        TODO("Not yet implemented")
                    }
                })

        }
    }

    fun createActivity() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.activityRepository.create(activityFormViewModel.toActivityPayload())
                .enqueue(object : ApiCallback<Activity>() {
                    override fun onSuccess(response: Activity) {
                        createdActivity = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }
                })
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
class ActivityFormViewModel {
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
            true
        },
        errorMessage = {
            "Choisissez une date de début postérieure à la date d'aujourd'hui"
        }
    ))

    var endDate by mutableStateOf(TextFieldState<Instant>(
        defaultValue = Instant.now(),
        validator = {
            it.isAfter(startDate.value)
        },
        errorMessage = {
            "Date de fin requise apres ${startDate.value}"
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
    var activityResources by mutableStateOf(TextFieldState<List<ActivityResourcePayload>>(
        defaultValue = listOf(),
        validator = {
            true
        },
        errorMessage = {
            ""
        }
    ))


    val stepsValidator by mutableStateOf(
        listOf(
            { name.isValid() && description.isValid() },
            { paddyFields.isValid() },
            { workers.isValid() },
            { activityResources.isValid() }
        )
    )

    val errorsStepMessage by mutableStateOf(
        listOf(
            {
                if (name.isValid() && description.isValid() && startDate.isValid() && endDate.isValid()) ""
                else "Veuillez remplir  les champs obligatoires"
            },
            { if (paddyFields.isValid()) "" else "Veuillez selectionner au moins une riziere" },
            { if (workers.isValid()) "" else "Veuillez selectionner au moins un travailleur" },
            { if (activityResources.isValid()) "" else "Veuillez selectionner au moins un ressource" }
        )
    )


    fun convertInstantToReadableHumanDate(instant: Instant): String {
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.FRANCE)
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }


    fun toActivityPayload(): ActivityPayload {
        return ActivityPayload(
            name = name.value,
            description = description.value,
            startDate = startDate.value.toString(),
            endDate = endDate.value.toString(),
            paddyFields = paddyFields.value.map { it.code },
            workers = workers.value.map { it.code },
            resources = activityResources.value.map {
                ActivityResourcePayload(resourceCode = it.resourceCode, quantity = it.quantity)
            }
        )
    }
}

