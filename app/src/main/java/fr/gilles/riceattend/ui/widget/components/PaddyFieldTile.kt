package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gilles.riceattend.services.entities.models.PaddyField

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaddyFieldTile(paddyField: PaddyField,
                   onClick: () -> Unit = {},
                   onLongClick: () -> Unit = {},
                   badgeContent: @Composable () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Icon(
            Icons.Outlined.Landscape,
            "paddy field icon",
            modifier = Modifier.padding(start = 15.dp)
        )
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(paddyField.name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                badgeContent()
            }
            Text(text = "Plant cultivable: " + paddyField.plant.name)
            Text(text = "Surface cultivable: " + paddyField.surface.value.toString() + " " + paddyField.surface.unit)
            Text(text = "Nombre de plants: " + paddyField.numberOfPlants.toString() + " plants")
        }
    }
}