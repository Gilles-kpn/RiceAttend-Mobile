package fr.gilles.riceattend.ui.screens.main.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun DashboardFragment(onMenuClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.primary),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick, modifier = Modifier.padding(end = 16.dp)) {
                Icon(Icons.Outlined.Menu, "Back ", tint = MaterialTheme.colors.background)
            }
            Text(
                text = "Tableau de Board",
                fontSize = 17.sp,
                color = MaterialTheme.colors.background
            )
        }
        Text(
            "Mes Statistiques",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 15.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp)),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Activites")
                Text(text = "2000", fontSize = 30.sp, modifier = Modifier.padding(bottom = 20.dp))
                Divider()
                Row(
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(text = "Ouvriers")
                        Text(text = "40", fontSize = 30.sp)
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(10.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(text = "Rizieres")
                        Text(text = "30", fontSize = 30.sp)
                    }

                }
            }

        }
        Text(
            "Mes Analyses",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 15.dp)
        )


    }

}

