package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.gilles.riceattend.R

@Composable
@Preview
fun LoadingCard() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IncludeLottieFile(draw = R.raw.loading, modifier = Modifier.size(100.dp, 100.dp))
    }
}


@Composable
@Preview
fun EmptyCard() {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IncludeLottieFile(draw = R.raw.empty, modifier = Modifier.size(200.dp, 200.dp))
        Text("Aucun élément à afficher")
    }
}