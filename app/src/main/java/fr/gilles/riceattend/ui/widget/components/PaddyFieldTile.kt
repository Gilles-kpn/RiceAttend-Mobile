package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Spa
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.gilles.riceattend.models.ActivityStatus
import fr.gilles.riceattend.models.PaddyField

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaddyFieldTile(
    paddyField: PaddyField,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    badgeContent: @Composable () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable { onClick() },
        elevation = 8.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        ) {
            Box(
                Modifier
                    .padding(horizontal = 10.dp)
                    .weight(2f)
            ) {
                AsyncImage(
                    model = paddyField.plant.image,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .weight(8f)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        paddyField.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    badgeContent()
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Spa, "plant",
                        Modifier
                            .padding(end = 7.dp)
                            .size(13.dp))
                    Text(
                        paddyField.plant.name,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
                Text(
                    text = "Surface cultivable: " + paddyField.surface.value.toString() + " " + paddyField.surface.unit,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

            }
        }
    }

}