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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
@Preview("LineChart")
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

        LineChartView()


    }

}

@Composable
fun LineChartView() {
    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(top = 20.dp, bottom = 20.dp, end = 20.dp),
            lineChartData = LineChartData(
                points = listOf(
                    LineChartData.Point(10F, "Line 1"),
                    LineChartData.Point(20F, "Line 2"),
                    LineChartData.Point(2F, "Line 3"),
                    LineChartData.Point(7F, "Line 4"),
                    LineChartData.Point(50F, "Line 5"),
                    LineChartData.Point(30F, "Line 6"),
                    LineChartData.Point(10F, "Line 7")
                )
            ),
            animation = simpleChartAnimation(),
            pointDrawer = FilledCircularPointDrawer(diameter = 2.dp, color = MaterialTheme.colors.primary),
            lineDrawer =SolidLineDrawer(color = MaterialTheme.colors.primary, thickness = 2.dp),
            xAxisDrawer = SimpleXAxisDrawer(
                axisLineColor = MaterialTheme.colors.primary,
                labelTextColor = MaterialTheme.colors.primary,
            ),
            yAxisDrawer = SimpleYAxisDrawer(
                drawLabelEvery = 5,
                axisLineColor = MaterialTheme.colors.primary,
                labelTextColor = MaterialTheme.colors.primary
            ),
            horizontalOffset = 0f,
        )
    }

}

