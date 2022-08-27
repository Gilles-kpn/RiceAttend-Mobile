package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.widget.ErrorText

@Composable
fun InputNumberWidget(
    state: TextFieldState<Int>,
    title: String,
    icon: ImageVector = Icons.Default.ShortText,
    trailing: @Composable () -> Unit = {},
    singleLine: Boolean = true,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    onChange: (Int) -> Unit = {},
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = roundedCornerShape,
        value = state.value.toString(),
        onValueChange = {
            try {
                if (it.isNotEmpty()) {
                    state.value = it.toInt()
                    onChange(it.toInt())
                } else {
                    state.value = 0
                }
            } catch (e: NumberFormatException) {

            }
            state.validate()
        },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(icon, contentDescription = "field Icon")
                Text(title)
            }
        },
        isError = state.error != null,
        trailingIcon = { trailing() },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
    state.error?.let {
        ErrorText(text = it)
    }
}