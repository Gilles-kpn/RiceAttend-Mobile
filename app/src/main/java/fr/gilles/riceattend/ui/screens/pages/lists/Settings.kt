package fr.gilles.riceattend.ui.screens.pages.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import fr.gilles.riceattend.services.storage.RiceAttendTheme
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.services.storage.themeFromString
import fr.gilles.riceattend.ui.viewmodel.SettingsVM
import fr.gilles.riceattend.ui.widget.components.AppBar

@Composable
fun SettingsFragment(
    viewModel: SettingsVM = hiltViewModel(),
    navHostController: NavHostController
) {
    Column(modifier= Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        AppBar(
            title = "Paramètres",
            leftContent = {
                IconButton(
                    onClick = {
                        navHostController.popBackStack()
                    },
                ){
                    Icon(
                        Icons.Filled.ArrowBack,
                        "Back",
                    )
                }
            }
        )

        //ui switch dark/light mode
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Selectionner le theme", style = MaterialTheme.typography.body1,)
                Text(text = "Actuel: ${themeFromString(SessionManager.session.preferences["theme"] ?: "system").label}", style = MaterialTheme.typography.caption)
                val availableTheme = listOf(
                    RiceAttendTheme.LIGHT,
                    RiceAttendTheme.DARK,
                    RiceAttendTheme.SYSTEM
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 5.dp, start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    availableTheme.forEach {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = SessionManager.session.preferences["theme"] == it.value,
                                onClick = {
                                    SessionManager.session = SessionManager.session.copy(preferences = SessionManager.session.preferences + mapOf("theme" to it.value))
                                    SessionManager.store()
                                }
                            )
                            Text(text = it.label, style = MaterialTheme.typography.body1)
                        }
                    }
                }
            }

        }

    }
}

enum class Language(val value: String, val label: String) {
    FRENCH("French", "Français"),
    ENGLISH("English", "English")
}