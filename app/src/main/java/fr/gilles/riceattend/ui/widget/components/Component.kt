package fr.gilles.riceattend.ui.widget.components

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.SecureFlagPolicy
import coil.compose.rememberAsyncImagePainter
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.screens.main.fragments.PaddyFieldFormViewModel
import fr.gilles.riceattend.ui.screens.main.fragments.WorkerFormViewModel
import fr.gilles.riceattend.ui.widget.ErrorText
import java.time.LocalTime
import java.util.*

@Composable
fun ActivityTile(onClick: () -> Unit = {}) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .padding(7.dp)
        .clickable { onClick() }) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier =  Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(2f), horizontalAlignment = Alignment.CenterHorizontally){
                Text("Start Date", textAlign = TextAlign.Center)
                //vertical Divider
                Divider(
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(10.dp)
                        .weight(7f)
                        .padding(vertical = 2.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(MaterialTheme.colors.primary, Color.White),
                                startY = 170f * 0.5f,
                                tileMode = TileMode.Clamp
                            )
                        )
                )
                Text("End Date", textAlign = TextAlign.Center)
            }

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(7f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nom de l'activite", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(
                        text = "En cours", modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                            .padding(all = 5.dp), color = MaterialTheme.colors.background
                    )
                }
                Text(
                    text = "De 20 Janvier 2020 - 27 Janvier 2020",
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(text = "16 Ouvriers - 2 Rizieres - Couts: 300 000FCFA")
            }
        }

    }
}


@Composable
fun ShowDatePicker(onDateSelected: (Date, LocalTime) -> Unit  = {_, _ ->},
                   initialDate: Date? = null
){

}



@Composable
fun WorkerTile(worker: Worker, onClick: (worker: Worker) -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick(worker) }
            .padding(start = 10.dp)
    ) {
        Icon(Icons.Outlined.Person, "worker icon")
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(worker.name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    text = worker.hourlyPay.toString() + " FCFA/H", modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                        .padding(all = 5.dp), color = MaterialTheme.colors.background
                )
            }
            Text(text = worker.email, modifier = Modifier.padding(bottom = 5.dp))
            Text(text = worker.phone)
        }
    }

}

@Composable
fun PaddyFieldTile(paddyField: PaddyField, onClick: () -> Unit = {}) {
    Log.d("paddyfield", "paddyfield  ==> $paddyField")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Icon(
            Icons.Outlined.Landscape,
            "paddy field icon",
            modifier = Modifier.padding(start = 15.dp)
        )
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(paddyField.name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    text = "Occupe", modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                        .padding(all = 5.dp), color = MaterialTheme.colors.background
                )
            }
            Text(text = "Plant cultivable: " + paddyField.plant.name)
            Text(text = "Surface cultivable: " + paddyField.surface.value.toString() + " " + paddyField.surface.unit)
            Text(text = "Nombre de plants: " + paddyField.numberOfPlants.toString() + " plants")
        }
    }
}


@Composable
fun OpenDialog(
    content: @Composable() () -> Unit = {},
    title: String = "Alert", onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    show: Boolean = false
) {
    if (show)

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                securePolicy = SecureFlagPolicy.SecureOff
            ),
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier.padding(bottom = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        content()
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(30.dp),
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        )


}


@Composable
fun LoadingCard() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(text = "Chargement...", style = MaterialTheme.typography.body1)
    }
}

@Composable
fun InputNumberWidget(
    state: TextFieldState<Int>,
    title: String,
    icon: ImageVector = Icons.Default.ShortText,
    trailing: @Composable () -> Unit = {},
    singleLine: Boolean = true,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(10.dp)
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = roundedCornerShape,
        value = state.value.toString(),
        onValueChange = {
            try {
                if (it.isNotEmpty()) {
                    state.value = it.toInt()
                } else {
                    state.value = 0
                }
            } catch (e: NumberFormatException) {

            }
            state.validate()
        },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(icon, contentDescription = "field Icon")
                Text(title)
            }
        },
        isError = state.error != null,
        trailingIcon = { trailing() },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
    state.error?.let {
        ErrorText(text = it)
    }
}

@Composable
fun InputWidget(
    state: TextFieldState<String>,
    title: String,
    icon: ImageVector = Icons.Default.ShortText,
    trailing: @Composable () -> Unit = {},
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(10.dp),
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = roundedCornerShape,
        value = state.value,
        onValueChange = {
            state.value = it
            state.validate()
        },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(icon, contentDescription = "field Icon")
                Text(title)
            }
        },
        isError = state.error != null,
        trailingIcon = { trailing() },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
    state.error?.let {
        ErrorText(text = it)
    }
}


@Composable
fun <T> InputDropDownSelect(
    state: TextFieldState<T>,
    list: List<T>,
    template: @Composable (T) -> Unit,
    title: String,
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val defaultSelectedIndex = remember {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.body1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(9f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        expanded.value = true
                    }) { template(list[defaultSelectedIndex.value]) }
            Box(modifier = Modifier.weight(1f)) {
                IconButton(onClick = { expanded.value = true }) {
                    Icon(Icons.Outlined.ArrowDropDown, "Dropdown")
                }
            }
        }
    }
    if (expanded.value)
        DropDownSelect(
            list = list,
            template = template,
            onIndexChanged = {
                defaultSelectedIndex.value = it
            },
            onSelected = {
                state.value = it
                state.validate()
                expanded.value = false
            },
            expanded = expanded.value,
            onDismiss = {
                expanded.value = false
            }
        )
    state.error?.let {
        ErrorText(text = it)
    }

}

@Composable
fun <T> DropDownSelect(
    list: List<T>,
    expanded: Boolean = false,
    template: @Composable (T) -> Unit,
    onIndexChanged: (Int) -> Unit,
    onSelected: (T) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        list.forEachIndexed { index, item ->
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onIndexChanged(index)
                    onSelected(item)
                }
            ) {
                template(item)
            }
        }
    }
}

@Composable

fun AppBar(
    title: String = "Page",
    leftContent: @Composable () -> Unit = {},
    rightContent: @Composable () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leftContent()
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.background,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        rightContent()
    }
}

@Composable
fun WorkerForm(
    workerFormViewModel: WorkerFormViewModel = WorkerFormViewModel(),
    onSubmit: () -> Unit = {},
    isLoading: Boolean = false,
    buttonText: String = "Créer",
    title: String = "Créer un Ouvrier"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Person, "Landscape")
            Text(title, style = MaterialTheme.typography.h6)
        }

        InputWidget(
            state = workerFormViewModel.firstNameState,
            title = "Nom"
        )
        InputWidget(
            state = workerFormViewModel.lastNameState,
            title = "Prénom"
        )
        InputWidget(
            state = workerFormViewModel.emailState,
            title = "Email",
            icon = Icons.Outlined.Email
        )
        InputWidget(
            state = workerFormViewModel.phoneState,
            title = "Téléphone",
            icon = Icons.Outlined.Phone
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text("Informations d'adresse", style = MaterialTheme.typography.h6)
                InputWidget(
                    state = workerFormViewModel.addressCountryState,
                    title = "Pays",
                    icon = Icons.Outlined.Map
                )
                InputWidget(
                    state = workerFormViewModel.addressCityState,
                    title = "Ville",
                    icon = Icons.Outlined.Map
                )
                InputWidget(
                    state = workerFormViewModel.addressStreetState,
                    title = "Rue",
                    icon = Icons.Outlined.Map
                )
            }
        }
        InputNumberWidget(
            state = workerFormViewModel.hourlyPayState,
            title = "Paye horaire",
            icon = Icons.Outlined.Money
        )
        Button(
            enabled = !isLoading && (
                    workerFormViewModel.firstNameState.isValid() &&
                            workerFormViewModel.lastNameState.isValid() &&
                            workerFormViewModel.emailState.isValid() &&
                            workerFormViewModel.phoneState.isValid() &&
                            workerFormViewModel.addressCountryState.isValid() &&
                            workerFormViewModel.addressCityState.isValid() &&
                            workerFormViewModel.addressStreetState.isValid() &&
                            workerFormViewModel.hourlyPayState.isValid()
                    ),
            onClick = { onSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(buttonText, style = MaterialTheme.typography.button)
            }
        }

    }
}

@Composable
fun ResourceTile(
    resource: Resource,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically
    ) {
        when (resource.resourceType) {
            ResourceType.WATER -> {
                Icon(Icons.Outlined.Water, "Water", Modifier.padding(horizontal = 20.dp))
            }
            ResourceType.MATERIALS -> {
                Icon(Icons.Outlined.Build, "Materials", Modifier.padding(horizontal = 20.dp))
            }
            ResourceType.OTHER -> {
                Icon(Icons.Outlined.Build, "Other", Modifier.padding(horizontal = 20.dp))
            }
            ResourceType.FERTILIZER -> {
                Icon(Icons.Outlined.Spa, "Fertilizer", Modifier.padding(horizontal = 20.dp))
            }
        }
        Column {
            Text(resource.name, style = MaterialTheme.typography.h6)
            Text("Quantité Restante: ${resource.quantity}")
            Text("Prix Unitaire: ${resource.unitPrice}")
            Text("Prix Total: ${resource.unitPrice * resource.quantity}")
        }
    }
}

@Composable
fun PaddyFieldForm(
    paddyFormViewModel: PaddyFieldFormViewModel = PaddyFieldFormViewModel(),
    plants: Page<Plant>? = null,
    onClick: () -> Unit = {},
    isLoading: Boolean = false,
    title: String = "Créer une rizière",
    buttonText: String = "Créer"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Landscape, "Landscape")
            Text(title, style = MaterialTheme.typography.h6)
        }
        plants?.let {
            InputDropDownSelect(
                state = paddyFormViewModel.plant,
                list = it.content,
                title = "Type de plant cultivable",
                template = { plant ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        //image using coil at left and column at right
                        Image(
                            painter = rememberAsyncImagePainter(plant.image),
                            contentDescription = "Profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp, 50.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.padding(start = 10.dp)) {
                            Text(
                                text = plant.name,
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                text = plant.shape,
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                text = plant.color,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            )
        }
        InputWidget(
            state = paddyFormViewModel.name,
            title = "Nom de la rizière",
        )
        InputNumberWidget(
            state = paddyFormViewModel.numberOfPlants,
            title = "Nombre de plants",
        )
        InputWidget(
            state = paddyFormViewModel.description,
            title = "Description",
            singleLine = false
        )
        Text(
            text = "Surface de la rizière",
            style = MaterialTheme.typography.body1
        )
        Row(Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1.5f)) {
                InputNumberWidget(
                    state = paddyFormViewModel.surface_value,
                    title = "Surface"
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                InputDropDownSelect(state = paddyFormViewModel.surface_unit,
                    list = listOf("m²", "ha"),
                    title = "Unité de mesure",
                    template = {
                        Text(
                            it,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                )
            }
        }
        Button(
            enabled = !isLoading && (
                    paddyFormViewModel.name.isValid() &&
                            paddyFormViewModel.numberOfPlants.isValid() &&
                            paddyFormViewModel.surface_value.isValid() &&
                            paddyFormViewModel.surface_unit.isValid() &&
                            paddyFormViewModel.plant.isValid()
                    ),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(50.dp)
                .clickable {
                }
        ) {
            if (isLoading)
                CircularProgressIndicator()
            else
                Text(buttonText, style = MaterialTheme.typography.button)
        }
    }
}

