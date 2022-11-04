package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import fr.gilles.riceattend.services.entities.models.Page
import fr.gilles.riceattend.services.entities.models.Plant
import fr.gilles.riceattend.ui.viewmodel.PaddyFieldFormVM

@Composable
fun PaddyFieldForm(
    paddyFormViewModel: PaddyFieldFormVM = PaddyFieldFormVM(),
    plants: Page<Plant>? = null,
    onClick: () -> Unit = {},
    isLoading: Boolean = false,
    title: String = "Créer une rizière",
    buttonText: String = "Créer"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Landscape, "Landscape")
            Text(title, style = MaterialTheme.typography.h6)
        }
        plants?.let {
            InputDropDownSelect(
                state = paddyFormViewModel.plant,
                list = it.content,
                title = "Type de plant cultivable",
                template = { plant ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        //image using coil at left and column at right
                        Image(
                            painter = rememberAsyncImagePainter(plant.image),
                            contentDescription = "Profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(50.dp, 50.dp).clip(CircleShape)
                        )
                        Column(modifier = Modifier.padding(start = 10.dp)) {
                            Text(
                                text = plant.name,
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                text = plant.shape,
                                fontSize= 13.sp,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            )
        }
        InputWidget(
            state = paddyFormViewModel.name,
            title = "Nom de la rizière",
        )
        InputNumberWidget(
            state = paddyFormViewModel.numberOfPlants,
            title = "Nombre de plants",
        )
        InputWidget(
            state = paddyFormViewModel.description,
            title = "Description",
            singleLine = false
        )
        Text(
            text = "Surface de la rizière",
            style = MaterialTheme.typography.body1
        )
        InputNumberWidget(
            state = paddyFormViewModel.surface_value,
            title = "Surface"
        )

        InputDropDownSelect(state = paddyFormViewModel.surface_unit,
            list = listOf("m²", "ha"),
            title = "Unité de mesure",
            template = {
                Text(
                    it,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        )
        Button(
            enabled = !isLoading && (
                    paddyFormViewModel.name.isValid() &&
                            paddyFormViewModel.numberOfPlants.isValid() &&
                            paddyFormViewModel.surface_value.isValid() &&
                            paddyFormViewModel.surface_unit.isValid() &&
                            paddyFormViewModel.plant.isValid()
                    ),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(50.dp)
                .clickable {
                }
        ) {
            if (isLoading)
                CircularProgressIndicator()
            else
                Text(buttonText, style = MaterialTheme.typography.button)
        }
    }
}