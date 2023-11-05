package com.phincon.qris.screen.portfolio

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.phincon.qris.MainActivity
import com.phincon.qris.R
import com.phincon.qris.model.HistoryChartModel
import com.phincon.qris.ui.theme.*
import com.phincon.qris.utils.getDonutChartData

private fun convertJsonToMap(): Map<String, Int> {
    val json = """
[
    {
        "type": "donutChart",
        "data": [
            {
                "label": "Tarik Tunai",
                "percentage": "55",
                "data": [
                    {
                        "trx_date": "21/01/2023",
                        "nominal": 1000000
                    },
                    {
                        "trx_date": "20/01/2023",
                        "nominal": 500000
                    },
                    {
                        "trx_date": "19/01/2023",
                        "nominal": 1000000
                    }
                ]
            },
            {
                "label": "QRIS Payment",
                "percentage": "31",
                "data": [
                    {
                        "trx_date": "21/01/2023",
                        "nominal": 159000
                    },
                    {
                        "trx_date": "20/01/2023",
                        "nominal": 35000
                    },
                    {
                        "trx_date": "19/01/2023",
                        "nominal": 1500
                    }
                ]
            },
            {
                "label": "Topup Gopay",
                "percentage": "7.7",
                "data": [
                    {
                        "trx_date": "21/01/2023",
                        "nominal": 200000
                    },
                    {
                        "trx_date": "20/01/2023",
                        "nominal": 195000
                    },
                    {
                        "trx_date": "19/01/2023",
                        "nominal": 5000000
                    }
                ]
            },
            {
                "label": "Lainnya",
                "percentage": "6.3",
                "data": [
                    {
                        "trx_date": "21/01/2023",
                        "nominal": 1000000
                    },
                    {
                        "trx_date": "20/01/2023",
                        "nominal": 500000
                    },
                    {
                        "trx_date": "19/01/2023",
                        "nominal": 1000000
                    }
                ]
            }
        ]
    },
    {
        "type": "lineChart",
        "data": {
            "month": [3, 7, 8, 10, 5, 10, 1, 3, 5, 10, 7, 7]
        }
    }
]
"""

    val gson = Gson()
    val chartDataList = gson.fromJson(json, Array<HistoryChartModel>::class.java).toList()
    Log.d("chart Data List", chartDataList.toString())

    val historyChartModels: List<HistoryChartModel> = chartDataList

    val donutChartDataList = historyChartModels.filter { it.type == "donutChart" }

    val donutChartMap = donutChartDataList.flatMap {
        if (it.data is List<*>) {
            val dataList = it.data as List<*>
            dataList.filterIsInstance<Map<*, *>>().map { data ->
                val label = data["label"] as String
                val percentage = (data["percentage"] as String).toDouble().toInt()
                label to percentage
            }
        } else {
            emptyList()
        }
    }.toMap()
    return donutChartMap
}


@Composable
fun PortfolioScreen(
    radiusOuter: Dp = 90.dp,
    chartBarWidth: Dp = 20.dp,
    animDuration: Int = 1000,
) {

    val donutDataList = getDonutChartData()
    val donutDataMap = donutDataList.flatMap {
        if (it.data is List<*>) {
            val dataList = it.data as List<*>
            dataList.filterIsInstance<Map<*, *>>().map { data ->
                val label = data["label"] as String
                val percentage = (data["percentage"] as String).toDouble().toInt()
                label to percentage
            }
        } else {
            emptyList()
        }
    }.toMap()

    val totalSum = donutDataMap.values.sum()
    val floatValue = mutableListOf<Float>()
    donutDataMap.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    val colors = listOf(
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Cyan,
        Color.Red
    )

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }

        // To see the data in more structured way
        // Compose Function in which Items are showing data
        DetailsPieChart(
            data = donutDataMap,
            colors = colors
        )

    }

}

@Composable
fun DetailsPieChart(
    data: Map<String, Int>,
    colors: List<Color>
) {
    Column(
        modifier = Modifier
            .padding(top = 80.dp)
            .fillMaxWidth()
    ) {
        // create the data items
        data.values.forEachIndexed { index, value ->
            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(index), value),
                color = colors[index]
            )
        }

    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Int>,
    height: Dp = 45.dp,
    color: Color
) {
    val activity = LocalView.current.context as? MainActivity
    Surface(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 40.dp),
        color = Color.Transparent
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(height)
                    .clickable {
                        activity?.navigateToChartDetail(data.first)
                        Log.d("Clicked Data", data.first)
                    }
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.first,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_semibold_600)
                    ),
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "${data.second}%",
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_medium_500)
                    ),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

        }

    }

}