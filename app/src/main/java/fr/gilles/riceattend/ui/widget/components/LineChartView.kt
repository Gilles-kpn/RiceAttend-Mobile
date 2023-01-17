package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarConfig
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.common.dimens.ChartDimens
import fr.gilles.riceattend.models.ActivitiesData
import fr.gilles.riceattend.models.ActivityStatus
import me.bytebeats.views.charts.pie.PieChart
import me.bytebeats.views.charts.pie.PieChartData
import me.bytebeats.views.charts.pie.render.SimpleSliceDrawer
import me.bytebeats.views.charts.simpleChartAnimation


@Composable
fun LineChartView(activitiesData: ActivitiesData) {
    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(26.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(activitiesData.activitiesByCreatedDate.isEmpty()){
                EmptyCard()
            }else{
                BarChart(
                    barData = activitiesData.activitiesByCreatedDate.map { e ->
                        BarData(e.key, e.value.toFloat())
                    },
                    onBarClick = {},
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 60.dp)
                        .height(250.dp),
                    chartDimens = ChartDimens(
                        20.dp
                    ),
                    axisConfig = AxisConfig(
                        showXLabels = true,
                        showAxis = true,
                        showUnitLabels = true,
                        isAxisDashed = true,
                        xAxisColor = MaterialTheme.colors.secondary,
                        yAxisColor = MaterialTheme.colors.background,
                        textColor = MaterialTheme.colors.primary
                    ),
                    barConfig = BarConfig(
                        hasRoundedCorner = false
                    )
                )
                Text("Répartition des activités par mois", modifier = Modifier.padding(vertical = 10.dp))
            }


        }


    }



}

@Composable
fun ActivityByStatus(activitiesData: ActivitiesData) {
    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(activitiesData.activitiesByStatus.isEmpty()){
                EmptyCard()
            }else{
                PieChart(
                    pieChartData = PieChartData(
                        slices = activitiesData.activitiesByStatus.map { e -> PieChartData.Slice(
                            e.value.toFloat(),
                            when(e.key){
                                ActivityStatus.INIT -> Color.LightGray
                                ActivityStatus.CANCELLED, ActivityStatus.UNDONE -> MaterialTheme.colors.error
                                ActivityStatus.IN_PROGRESS -> Color(0xFFFF5722)
                                ActivityStatus.DONE -> MaterialTheme.colors.primary
                            }
                        ) }
                    ),
                    // Optional properties.
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(vertical = 10.dp),
                    animation = simpleChartAnimation(),
                    sliceDrawer = SimpleSliceDrawer( 100F)
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .horizontalScroll(rememberScrollState())) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp),  verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier
                            .size(width = 30.dp, height = 10.dp)
                            .background(Color.LightGray)
                            .clip(CircleShape))
                        Text(text = ActivityStatus.INIT.label+
                                "(${activitiesData.activitiesByStatus[ActivityStatus.INIT] ?: 0})")
                    }

                    Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp),  verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier
                            .size(width = 30.dp, height = 10.dp)
                            .background(MaterialTheme.colors.error)
                            .clip(CircleShape))
                        Text(text = ActivityStatus.UNDONE.label + "(${activitiesData.activitiesByStatus[ActivityStatus.UNDONE] ?: 0})")
                    }

                    Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp),  verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier
                            .size(width = 30.dp, height = 10.dp)
                            .background(Color(0xFFFF5722))
                            .clip(CircleShape))
                        Text(text = ActivityStatus.IN_PROGRESS.label + "(${activitiesData.activitiesByStatus[ActivityStatus.IN_PROGRESS] ?: 0})")
                    }

                    Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier
                            .size(width = 30.dp, height = 10.dp)
                            .background(MaterialTheme.colors.primary)
                            .clip(CircleShape))
                        Text(text = ActivityStatus.DONE.label + "(${activitiesData.activitiesByStatus[ActivityStatus.DONE] ?: 0})")
                    }
                }
                Text("Répartition des activités par status", modifier = Modifier.padding(vertical = 10.dp))
            }
        }


    }
}