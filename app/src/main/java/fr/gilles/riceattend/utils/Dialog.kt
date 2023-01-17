package fr.gilles.riceattend.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

class DialogService {
    companion object {
        var dialog: Dialog? by mutableStateOf(null)

        fun show(dialog: Dialog) {
            this.dialog = dialog
        }

    }
}

data class Dialog(
    val title: String,
    val message: String,
    val icon: ImageVector = Icons.Outlined.Info,
    val dismissText: String = "Annuler",
    val submitText: String = "Fermer",
    val onDismiss: () -> Unit = {},
    val onSuccess: () -> Unit = {},
    val displayDismissButton : Boolean = true,
    val displaySubmitButton : Boolean = true
)