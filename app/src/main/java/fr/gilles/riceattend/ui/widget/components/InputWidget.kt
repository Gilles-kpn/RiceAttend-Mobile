package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.widget.ErrorText

@Composable
fun InputWidget(
    state: TextFieldState<String>,
    title: String,
    icon: ImageVector = Icons.Default.ShortText,
    trailing: @Composable () -> Unit = {},
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column() {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = roundedCornerShape,
            value = state.value,
            onValueChange = {
                state.value = it
                state.validate()
            },
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(icon, contentDescription = "field Icon", Modifier.padding(horizontal = 10.dp))
                    Text(title, fontSize = 12.sp)
                }
            },
            isError = state.error != null,
            trailingIcon = { trailing() },
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            visualTransformation = visualTransformation
        )
        state.error?.let {
            ErrorText(text = it)
        }
    }

}