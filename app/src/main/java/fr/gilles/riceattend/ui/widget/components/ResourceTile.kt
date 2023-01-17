package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Water
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gilles.riceattend.models.Resource
import fr.gilles.riceattend.models.ResourceType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResourceTile(
    resource: Resource,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    function:@Composable () -> Unit = {
        Text(
            "Quantité Restante: ${resource.quantity}",
            modifier = Modifier.padding(vertical = 4.dp),
            fontSize = 12.sp
        )
        Text(
            "Prix Unitaire: ${resource.unitPrice}",
            modifier = Modifier.padding(vertical = 3.dp),
            fontSize = 12.sp
        )
    }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable { onClick() },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically
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
                Text(
                    resource.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                function()

            }
        }
    }
}