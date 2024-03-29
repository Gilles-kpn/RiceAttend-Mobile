package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.widget.ErrorText

@Composable
fun <T> InputDropDownSelect(
    state: TextFieldState<T>,
    list: List<T>,
    template: @Composable (T) -> Unit,
    title: String,
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val defaultSelectedIndex = remember {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.body1)
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth().clickable { if(list.isNotEmpty()) expanded.value = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (list.isEmpty()) Text(text = "Aucun element a selectionner")
            else template(list[defaultSelectedIndex.value])
            if (expanded.value)
                DropDownSelect(
                    list = list,
                    template = template,
                    onIndexChanged = {
                        defaultSelectedIndex.value = it
                    },
                    onSelected = {
                        state.value = it
                        state.validate()
                        expanded.value = false
                    },
                    expanded = expanded.value
                ) {
                    expanded.value = false
                }
            else Icon(Icons.Outlined.ArrowDropDown, "Dropdown")
        }
    }

    state.error?.let {
        ErrorText(text = it)
    }

}

@Composable
fun <T> DropDownSelect(
    list: List<T>,
    expanded: Boolean = false,
    template: @Composable (T) -> Unit,
    onIndexChanged: (Int) -> Unit,
    onSelected: (T) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth(0.98f)
            .padding(12.dp),
        properties = PopupProperties(focusable = true, clippingEnabled = true)
    ) {
        list.forEachIndexed { index, item ->
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onIndexChanged(index)
                    onSelected(item)
                }
            ) {
                template(item)
            }
        }
    }
}