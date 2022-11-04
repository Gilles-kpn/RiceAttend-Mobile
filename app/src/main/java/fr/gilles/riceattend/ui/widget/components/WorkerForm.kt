package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.ui.viewmodel.WorkerFormVM

@Composable
fun WorkerForm(
    workerFormVM: WorkerFormVM = remember { WorkerFormVM() },
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
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Person, "Landscape")
            Text(title, style = MaterialTheme.typography.h6)
        }

        InputWidget(
            state = workerFormVM.firstNameState,
            title = "Nom"
        )
        InputWidget(
            state = workerFormVM.lastNameState,
            title = "Prénom"
        )
        InputWidget(
            state = workerFormVM.emailState,
            title = "Email",
            icon = Icons.Outlined.Email
        )
        InputWidget(
            state = workerFormVM.phoneState,
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
                    state = workerFormVM.addressCountryState,
                    title = "Pays",
                    icon = Icons.Outlined.Map
                )
                InputWidget(
                    state = workerFormVM.addressCityState,
                    title = "Ville",
                    icon = Icons.Outlined.Map
                )
                InputWidget(
                    state = workerFormVM.addressStreetState,
                    title = "Rue",
                    icon = Icons.Outlined.Map
                )
            }
        }
        InputNumberWidget(
            state = workerFormVM.hourlyPayState,
            title = "Paye horaire",
            icon = Icons.Outlined.Money
        )
        Button(
            enabled = !isLoading && (
                    workerFormVM.firstNameState.isValid() &&
                            workerFormVM.lastNameState.isValid() &&
                            workerFormVM.emailState.isValid() &&
                            workerFormVM.phoneState.isValid() &&
                            workerFormVM.addressCountryState.isValid() &&
                            workerFormVM.addressCityState.isValid() &&
                            workerFormVM.addressStreetState.isValid() &&
                            workerFormVM.hourlyPayState.isValid()
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