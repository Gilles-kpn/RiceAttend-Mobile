package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.screens.main.fragments.Language

class SettingsVM {
    var isDarkMode by mutableStateOf(SessionManager.session.preferences["theme"] != "light")
    var language by mutableStateOf(TextFieldState(
        defaultValue = Language.FRENCH,
        errorMessage = { "" },
        validator = { true }
    ))

}