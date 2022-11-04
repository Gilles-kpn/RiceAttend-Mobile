package fr.gilles.riceattend.ui.widget.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarConfig
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.common.dimens.ChartDimens
import com.himanshoe.charty.horizontalbar.HorizontalBarChart
import com.himanshoe.charty.horizontalbar.axis.HorizontalAxisConfig
import com.himanshoe.charty.horizontalbar.config.HorizontalBarConfig
import com.himanshoe.charty.horizontalbar.config.StartDirection
import com.himanshoe.charty.horizontalbar.model.HorizontalBarData
import fr.gilles.riceattend.services.entities.models.ActivitiesData


@Composable
fun LineChartView(activitiesData: ActivitiesData) {
    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(16.dp)
    ) {

        BarChart(
            barData = activitiesData.activitiesByCreatedDate.map { e ->
                BarData(e.key, e.value.toFloat())
            },
            onBarClick = {},
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp)
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

    }



}

@Composable
fun ActivityByStatus(activitiesData: ActivitiesData) {
    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        HorizontalBarChart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .height(150.dp),
            onBarClick = { },
            barDimens = ChartDimens(20.dp),
            colors = listOf( MaterialTheme.colors.primary,MaterialTheme.colors.primary,),
            horizontalBarData = activitiesData.activitiesByStatus.map { e ->
                HorizontalBarData(e.value.toFloat(), e.key.label)
            },
            horizontalAxisConfig = HorizontalAxisConfig(
                showUnitLabels = true,
                showAxes = true,
                xAxisColor = MaterialTheme.colors.primary,
                yAxisColor = MaterialTheme.colors.primary
            ),
            horizontalBarConfig = HorizontalBarConfig(
                showLabels = true,
                startDirection = StartDirection.Left
            )
        )
    }
}