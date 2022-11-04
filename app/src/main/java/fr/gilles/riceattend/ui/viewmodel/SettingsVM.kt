package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.app.SessionManager
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.screens.main.lists.Language

@HiltViewModel
class SettingsVM : ViewModel() {
    var isDarkMode by mutableStateOf(SessionManager.session.preferences["theme"] != "light")
    var language by mutableStateOf(TextFieldState(
        defaultValue = Language.FRENCH,
        errorMessage = { "" },
        validator = { true }
    ))

}