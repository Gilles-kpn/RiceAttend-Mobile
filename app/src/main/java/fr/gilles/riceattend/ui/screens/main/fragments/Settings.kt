package fr.gilles.riceattend.ui.screens.main.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.ui.viewmodel.SettingsVM
import fr.gilles.riceattend.ui.widget.components.AppBar
import fr.gilles.riceattend.ui.widget.components.InputDropDownSelect

@Composable
fun SettingsFragment(
    onMenuClick: () -> Unit = {},
    viewModel: SettingsVM =  SettingsVM()
) {
    Column(modifier= Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        AppBar(
            title = "Paramètres",
            leftContent = {
                IconButton(
                    onClick = onMenuClick,
                ){
                    Icon(
                        Icons.Filled.ArrowBack,
                        "Back",
                        tint = MaterialTheme.colors.background
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
                Text("Mode Sombre", style = MaterialTheme.typography.body1,)
                //subtitle
                Text(text = "Actuel: ${if(viewModel.isDarkMode) "Sombre" else "Clair"}", style = MaterialTheme.typography.caption)
            }
            Switch(
                checked = viewModel.isDarkMode,
                onCheckedChange = { viewModel.isDarkMode = it }
            )
        }

        //ui switch dark/light mode
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.weight(5f)){
                Column {
                    Text("Langue", style = MaterialTheme.typography.body1,)
                    //subtitle
                    Text(text = "Selectionner la langue de votre choix", style = MaterialTheme.typography.caption)
                }
            }
            Box(
                Modifier.weight(2f),
            ){
                InputDropDownSelect(state = viewModel.language, list = listOf(
                    Language.ENGLISH,
                    Language.FRENCH,
                ), template = {
                    Text(
                        text = it.label,
                        modifier = Modifier.padding(10.dp)
                    )
                }, title ="" )
            }
        }

    }
}

enum class Language(val value: String, val label: String) {
    FRENCH("French", "Français"),
    ENGLISH("English", "English")
}