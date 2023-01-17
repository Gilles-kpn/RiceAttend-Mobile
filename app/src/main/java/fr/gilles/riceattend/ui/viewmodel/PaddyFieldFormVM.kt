package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.gilles.riceattend.models.PaddyFieldPayLoad
import fr.gilles.riceattend.models.Plant
import fr.gilles.riceattend.models.Surface
import fr.gilles.riceattend.ui.formfields.TextFieldState
class PaddyFieldFormVM {
    var name by mutableStateOf(
        TextFieldState(
            validator = { it.isNotBlank() && it.isNotEmpty() },
            errorMessage = { "Nom requis" },
            defaultValue = ""
        )
    )
    var description by mutableStateOf(TextFieldState(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Description requise"
    }, defaultValue = ""))
    var plant by mutableStateOf(TextFieldState(validator = {
        it.code.isBlank().not()
    }, errorMessage = {
        "Plant requise"
    }, defaultValue = Plant(
        name = "",
        description = "",
        image = "",
        color = "",
        shape = "",
        cultivationTime = null,
        variety = null
    )
    )
    )
    var numberOfPlants by mutableStateOf(TextFieldState(validator = {
        it >= 1
    }, errorMessage = {
        "Nombre de plantes requis"
    }, defaultValue = 1))
    var surface_value by mutableStateOf(TextFieldState<Int>(validator = {
        it >= 1
    }, errorMessage = {
        "Surface requise et doit être supérieur à 1"
    },
        defaultValue = 1
    )
    )
    var surface_unit by mutableStateOf(TextFieldState<String>(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Unité de surface requise"
    },
        defaultValue = "m²"
    )
    )


    fun toPaddyFieldPayload(): PaddyFieldPayLoad {
        return PaddyFieldPayLoad(
            name = name.value,
            description = description.value,
            plantCode = plant.value.code,
            numberOfPlants = numberOfPlants.value,
            surface = Surface(
                value = surface_value.value.toLong(),
                unit = surface_unit.value
            )
        )
    }

}