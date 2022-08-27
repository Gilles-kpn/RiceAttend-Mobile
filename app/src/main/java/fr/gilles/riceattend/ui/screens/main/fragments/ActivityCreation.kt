package fr.gilles.riceattend.ui.screens.main.fragments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fr.gilles.riceattend.R
import fr.gilles.riceattend.services.entities.models.ActivityResourcePayload
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.viewmodel.ActivityCreationVM
import fr.gilles.riceattend.ui.widget.ErrorText
import fr.gilles.riceattend.ui.widget.components.*
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityCreationScreen(
    viewModel: ActivityCreationVM = remember { ActivityCreationVM() },
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
                                text = viewModel.activityFormVM.errorsStepMessage[currentStep](),
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
                        state = viewModel.activityFormVM.name,
                        title = "Nom de l'activité (*)",
                    )
                    InputWidget(
                        state = viewModel.activityFormVM.description,
                        title = "Description de l'activité",
                        singleLine = false
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        value = viewModel.activityFormVM.convertInstantToReadableHumanDate(
                            viewModel.activityFormVM.startDate.value
                        ),
                        onValueChange = {
                            viewModel.activityFormVM.endDate.validate()
                        },
                        label = { Text("Date de début (*)") },
                        trailingIcon = {
                            IconButton(onClick = {
                                dateSelectListener = {
                                    viewModel.activityFormVM.startDate.value = it
                                    viewModel.activityFormVM.startDate.validate()
                                    showDateDialog = false
                                }
                                showDateDialog = true
                            }) {
                                Icon(Icons.Outlined.CalendarToday, "Calendrier")
                            }
                        },
                        isError = viewModel.activityFormVM.startDate.error != null,
                    )
                    viewModel.activityFormVM.startDate.error?.let {
                        ErrorText(text = it)
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        value = viewModel.activityFormVM.convertInstantToReadableHumanDate(
                            viewModel.activityFormVM.endDate.value
                        ),
                        onValueChange = {
                            viewModel.activityFormVM.endDate.validate()
                        },
                        label = { Text("Date de fin (*)") },
                        trailingIcon = {
                            IconButton(onClick = {
                                dateSelectListener = {
                                    viewModel.activityFormVM.endDate.value = it
                                    viewModel.activityFormVM.endDate.validate()
                                    showDateDialog = false
                                }
                                showDateDialog = true
                            }) {
                                Icon(Icons.Outlined.CalendarToday, "Calendrier")
                            }
                        },
                        isError = viewModel.activityFormVM.endDate.error != null,
                    )

                    viewModel.activityFormVM.endDate.error?.let {
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
                                                        checked = viewModel.activityFormVM.paddyFields.value.contains(
                                                            paddyField
                                                        ), onCheckedChange = { checked ->
                                                            if (checked) viewModel.activityFormVM.paddyFields.let { paddyFields ->
                                                                paddyFields.value =
                                                                    paddyFields.value + paddyField
                                                            }
                                                            else viewModel.activityFormVM.paddyFields.let { paddyFields ->
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
                                                        checked = viewModel.activityFormVM.workers.value.contains(
                                                            worker
                                                        ), onCheckedChange = { checked ->
                                                            if (checked) viewModel.activityFormVM.workers.let { workers ->
                                                                workers.value =
                                                                    workers.value + worker
                                                            }
                                                            else viewModel.activityFormVM.workers.let { workers ->
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
                                                    viewModel.activityFormVM.activityResources.value
                                                        .stream().anyMatch { activityResource ->
                                                            activityResource.resourceCode == resource.code
                                                        }
                                                Column {
                                                    Row(
                                                        Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Checkbox(
                                                            checked = viewModel.activityFormVM.activityResources.value
                                                                .stream()
                                                                .anyMatch { activityResource ->
                                                                    activityResource.resourceCode == resource.code
                                                                },
                                                            onCheckedChange = { checked ->
                                                                if (checked) viewModel.activityFormVM.activityResources.let { activityResource ->
                                                                    activityResource.value =
                                                                        activityResource.value + ActivityResourcePayload(
                                                                            resourceCode = resource.code,
                                                                            quantity = 1f
                                                                        )
                                                                }
                                                                else viewModel.activityFormVM.activityResources.let { activityResource ->
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
                                                                defaultValue = viewModel.activityFormVM.activityResources.value.stream()
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
                                                                viewModel.activityFormVM.activityResources.let { activityResource ->
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
                        if (viewModel.activityFormVM.stepsValidator[currentStep]()) {
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