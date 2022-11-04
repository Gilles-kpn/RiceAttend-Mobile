package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.entities.models.ResourcePayload
import fr.gilles.riceattend.services.entities.models.ResourceType
import fr.gilles.riceattend.ui.formfields.TextFieldState

@HiltViewModel
class ResourceFormVM {
    var loading by mutableStateOf(false)
    var name by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Nom requis" },
        )
    )

    var quantity by mutableStateOf(
        TextFieldState(
            defaultValue = 1,
            validator = { it > 0 },
            errorMessage = { "Quantie requise superieur a 1" },
        )
    )

    var price by mutableStateOf(
        TextFieldState(
            defaultValue = 1,
            validator = { it > 0 },
            errorMessage = { "Quantie requise superieur a 1" },
        )
    )

    var type by mutableStateOf(
        TextFieldState(
            defaultValue = ResourceType.WATER,
            validator = { it.value.isNotBlank() },
            errorMessage = { "Choisissez un type valide" },
        )
    )


    fun toResourcePayload(): ResourcePayload {
        return ResourcePayload(
            unitPrice = price.value.toLong(),
            name = name.value,
            resourceType = type.value,
            quantity = quantity.value.toLong()
        )
    }




}