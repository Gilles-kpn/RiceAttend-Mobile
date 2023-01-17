package fr.gilles.riceattend.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.gilles.riceattend.models.*
import fr.gilles.riceattend.ui.formfields.TextFieldState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class ActivityFormVM {
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

    var type by mutableStateOf(
        TextFieldState(
            defaultValue = ActivityType.OTHER,
            validator = {
                it.value.isNotBlank() && it.value.isNotEmpty()
            },
            errorMessage = {
                "Le type d'activite doit etre selectionner"
            }
        )
    )

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
            type = type.value,
            paddyFields = paddyFields.value.map { it.code },
            workers = workers.value.map { it.code },
            resources = activityResources.value.map {
                ActivityResourcePayload(resourceCode = it.resourceCode, quantity = it.quantity)
            }
        )
    }
}