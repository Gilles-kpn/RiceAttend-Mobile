package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.TimerOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.services.entities.models.Activity
import fr.gilles.riceattend.services.entities.models.ActivityStatus

@Composable
fun ActivityTile(onClick: () -> Unit = {}, activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable { onClick() },
        elevation = 8.dp
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            when (activity.status) {
                ActivityStatus.IN_PROGRESS -> {
                    Icon(
                        Icons.Outlined.Timer,
                        "In progress",
                        Modifier.padding(20.dp),
                        MaterialTheme.colors.primary
                    )
                }
                ActivityStatus.DONE -> {
                    Icon(Icons.Outlined.DoneAll, "Done", Modifier.padding(20.dp), Color.Green)
                }
                ActivityStatus.CANCELLED -> {
                    Icon(Icons.Outlined.Cancel, "Cancelled", Modifier.padding(20.dp), Color.Red)
                }
                ActivityStatus.UNDONE -> {
                    Icon(Icons.Default.Snooze, "Undone", Modifier.padding(20.dp), Color.Cyan)
                }
                else -> {
                    Icon(Icons.Outlined.TimerOff, "Unknown", Modifier.padding(20.dp))
                }
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(7f)
            ) {
                Text(activity.name, style = MaterialTheme.typography.h1)
                Text(
                    activity.status.label,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = "${formatDateToHumanReadable(activity.startDate)} - ${
                        formatDateToHumanReadable(
                            activity.endDate
                        )
                    }", modifier = Modifier
                )
                Text(
                    text = "Cree le ${formatDateToHumanReadable(activity.createdAt)}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }

    }
}