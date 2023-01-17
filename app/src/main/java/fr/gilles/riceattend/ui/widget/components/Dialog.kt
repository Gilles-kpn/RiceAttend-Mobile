package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.utils.DialogService

@Composable
fun RiceAttendDialog(){
    Box(
        Modifier
            .fillMaxSize()
            .clickable(enabled = false, onClick = {})
            .background(color = Color.Black.copy(alpha = 0.5f)),
        Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 15.dp),

                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(DialogService.dialog!!.icon, "dialog box")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = DialogService.dialog!!.title, fontSize = MaterialTheme.typography.h6.fontSize)
                }
                //content// lorem ipsum
                Text(
                    text = DialogService.dialog!!.message,
                    modifier = Modifier.padding(10.dp),
                )
                //button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp,),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(DialogService.dialog!!.displayDismissButton)
                    Button(
                        onClick = {
                            DialogService.dialog!!.onDismiss()
                            DialogService.dialog = null
                        },
                        Modifier
                            .weight(1f)
                            .height(55.dp)
                            .padding(5.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.error
                        )
                    ) {
                        Text(text = DialogService.dialog!!.dismissText)
                    }
                    if(DialogService.dialog!!.displaySubmitButton)
                        Button(
                            onClick = {
                                DialogService.dialog!!.onSuccess()
                                DialogService.dialog = null
                            },
                            Modifier
                                .weight(1f)
                                .height(55.dp)
                                .padding(5.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary
                            )
                        ) {
                            Text(text = DialogService.dialog!!.submitText)
                        }
                }
            }
        }
    }
}