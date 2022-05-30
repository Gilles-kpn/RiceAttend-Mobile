package fr.gilles.riceattend.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorText(text: String) {
    Text(
        text = text,
        color = Color.Red,
        fontSize = 12.sp,
        modifier = Modifier.padding(start = 8.dp)
    )
}