package fr.gilles.riceattend.ui.screens.main.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun ActivityModelScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(bottom = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.ArrowBack, "Back ", tint = MaterialTheme.colors.background)
                }
                Text(
                    text = "Nom de l'activite",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            Card(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .size(150.dp)
                    .clip(CircleShape)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Outlined.Timelapse, "Timelapse")
                    Text(text = "Duree Totale")
                    Text(text = "12 Jours", fontSize = 30.sp)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Debut", color = MaterialTheme.colors.background)
                    Text(text = "20-Janv-2020", color = MaterialTheme.colors.background)
                }
                Divider(color = MaterialTheme.colors.background, modifier = Modifier.width(100.dp))
                Column {
                    Text(text = "Fin", color = MaterialTheme.colors.background)
                    Text(text = "27-Janv-2020", color = MaterialTheme.colors.background)
                }

            }

        }
        var selectedIndex by remember {
            mutableStateOf(0)
        }
        val tabs = listOf("Ouvriers (20)", "Rizieres (10)")
        TabRow(
            selectedTabIndex = selectedIndex,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                ) {
                    Text(
                        text = tab,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedIndex) {
                0 -> {
                    Column(modifier = Modifier.fillMaxSize()) {
//                        Swipe { WorkerCard() }
                    }
                }
                1 -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        //paddy field here
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Swipe(content: @Composable () -> Unit) {
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default
    )
    SwipeToDismiss(
        state = dismissState,
        background = {
            val color = Color.Transparent
            val direction = dismissState.dismissDirection
            if (direction == DismissDirection.EndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.heightIn(5.dp))
                        Text(
                            text = "Retirer",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )

                    }
                }
            }
        },
        /**** Dismiss Content */
        dismissContent = {
            content()
        },
        /*** Set Direction to dismiss */
        directions = setOf(DismissDirection.EndToStart),
    )
}
