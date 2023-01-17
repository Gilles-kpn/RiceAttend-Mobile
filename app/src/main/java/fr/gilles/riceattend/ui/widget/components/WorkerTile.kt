package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.gilles.riceattend.models.Worker

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkerTile(
    worker: Worker,
    withBadge: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    additionnalContent:@Composable ()->Unit = { Text(
        text = worker.phone,
        fontSize = 12.sp
    )}
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .combinedClickable(onClick = onClick, onLongClick = onLongClick)
                .padding(start = 10.dp)
        ) {
            AsyncImage(
                model = userGravatar(email = worker.email),
                contentDescription = "Profile picture",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
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
                    Text(
                        worker.name, fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (withBadge)
                        Text(
                            text = worker.hourlyPay.toString() + " FCFA/H", modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.primary)
                                .padding(all = 5.dp),
                            color = MaterialTheme.colors.background,
                            fontSize = 12.sp
                        )
                }
                Text(
                    text = worker.email,
                    modifier = Modifier.padding(bottom = 5.dp),
                    fontSize = 12.sp
                )
               additionnalContent()
            }
        }
    }


}