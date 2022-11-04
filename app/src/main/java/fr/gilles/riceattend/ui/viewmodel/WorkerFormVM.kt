package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.entities.models.Address
import fr.gilles.riceattend.services.entities.models.WorkerPayload
import fr.gilles.riceattend.ui.formfields.EmailFieldState
import fr.gilles.riceattend.ui.formfields.TextFieldState

@HiltViewModel
class WorkerFormVM {

    var firstNameState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Nom requis" },
        )
    )
    var lastNameState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Prénom requis" },
        )
    )
    var phoneState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = {
                java.util.regex.Pattern.matches(
                    "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}\$",
                    it
                )
            },
            errorMessage = { "Téléphone requis" },
        )
    )
    var emailState by mutableStateOf(EmailFieldState())

    var hourlyPayState by mutableStateOf(
        TextFieldState(
            defaultValue = 1,
            validator = { it > 0 },
            errorMessage = { "Paye horaire requis superieur 1 FCFA" },
        )
    )

    var addressCountryState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Pays requis" },
        )
    )

    var addressCityState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Ville requise" },
        )
    )

    var addressStreetState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Rue requise" },
        )
    )

    fun toWorkerPayload(): WorkerPayload {
        return WorkerPayload(
            name = firstNameState.value + " " + lastNameState.value,
            firstName = firstNameState.value,
            lastName = lastNameState.value,
            phone = phoneState.value,
            email = emailState.value,
            hourlyPay = hourlyPayState.value,
            address = Address(
                country = addressCountryState.value,
                city = addressCityState.value,
                street = addressStreetState.value
            )
        )
    }
}