package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Water
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.services.entities.models.Resource
import fr.gilles.riceattend.services.entities.models.ResourceType

@Composable
fun ResourceTile(
    resource: Resource,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically
    ) {
        when (resource.resourceType) {
            ResourceType.WATER -> {
                Icon(Icons.Outlined.Water, "Water", Modifier.padding(horizontal = 20.dp))
            }
            ResourceType.MATERIALS -> {
                Icon(Icons.Outlined.Build, "Materials", Modifier.padding(horizontal = 20.dp))
            }
            ResourceType.OTHER -> {
                Icon(Icons.Outlined.Build, "Other", Modifier.padding(horizontal = 20.dp))
            }
            ResourceType.FERTILIZER -> {
                Icon(Icons.Outlined.Spa, "Fertilizer", Modifier.padding(horizontal = 20.dp))
            }
        }
        Column {
            Text(resource.name, style = MaterialTheme.typography.h1)
            Text("Quantité Restante: ${resource.quantity}")
            Text("Prix Unitaire: ${resource.unitPrice}")
        }
    }
}