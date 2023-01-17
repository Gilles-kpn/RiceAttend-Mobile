package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@Composable
fun OpenDialog(
    content: @Composable() () -> Unit = {},
    title: String = "Alert", onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    show: Boolean = false,
    isSuccess: Boolean = false,
) {
    if (show)
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                securePolicy = SecureFlagPolicy.SecureOff
            ),
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth().clip(RoundedCornerShape(10.dp)),
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSuccess)
                                Icon(
                                    Icons.Outlined.Check,
                                    "success icon",
                                    tint = MaterialTheme.colors.primary
                                )
                            else
                                Icon(
                                    Icons.Outlined.Error,
                                    "error icon",
                                    tint = MaterialTheme.colors.error
                                )
                            Text(
                                text = title,
                                modifier = Modifier.padding(horizontal = 10.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
                            content()
                        }
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            shape = RoundedCornerShape(30.dp),
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        )


}