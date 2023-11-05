package com.phincon.qris.screen.portfolio

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.phincon.qris.model.DataDonutType
import com.phincon.qris.model.HistoryChartModel
import com.phincon.qris.utils.PreferencesManager
import com.phincon.qris.utils.formatToRupiah
import com.phincon.qris.utils.getDonutChartData

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun PortfolioDetailScreen(navController: NavHostController, label: String) {
    var user by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(context) {
        val preferencesManager = PreferencesManager(context)
        user = preferencesManager.getName("name", "")
    }
    Scaffold(topBar = {
        Column {
            CenterAlignedTopAppBar(title = { Text("Riwayat Transaksi ${label}") })
            Divider()
        }
    }, bottomBar = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider()
            Button(
                onClick = {
                    val route = "main/${user}"
                    navController.navigate(route) {
                        popUpTo("main/${user}") {
                            inclusive = true
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Kembali")
            }
        }

    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color.White,
                    )
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,

                ){

                HistoryChartLazyColumn(items = getDataByTpe(label) )
            }
        }

    }
}

@Composable
@Preview
fun ChartDetailScreenPreview() {
//    ChartDetailScreen("")
}

@Composable
fun HistoryChartLazy(items: List<DataDonutType.DataDonutEntry>) {
    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState) {
        items(items) { item ->
            ListHistoryChart(
                item
            )
        }
    }
}

@Composable
fun ListHistoryChart(item: DataDonutType.DataDonutEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                1.dp,
                SolidColor(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(5.dp)
            )

        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = formatToRupiah(item.nominal) ?: "-",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = item.trxDate,
            fontSize = 14.sp,
            color = Color.Gray
        )


    }
}

@Composable
fun HistoryChartLazyColumn(items: List<DataDonutType.DataDonutEntry>) {
    HistoryChartLazy(items = items)
}


fun getDataByTpe(label: String) : List<DataDonutType.DataDonutEntry>{
    var listDataDonut  = mutableListOf<DataDonutType.DataDonutEntry>()
    val historyChartModels: List<HistoryChartModel> = getDonutChartData()
    val tarikTunaiDataList = historyChartModels
        .filter { it.type == "donutChart" } // Filter donutChart items
        .flatMap { historyChartModel ->
            if (historyChartModel.data is List<*>) {
                val dataList = historyChartModel.data as List<*>
                val tarikTunaiData = dataList.filterIsInstance<Map<*, *>>()
                    .filter { it["label"] == label }
                    .flatMap { it["data"] as List<*> }
                    .filterIsInstance<Map<*, *>>()
                    .map { dataEntry ->
                        val trxDate = dataEntry["trx_date"] as? String
                        val nominal = dataEntry["nominal"] as? Double
                        Pair(trxDate, nominal)
                    }
                tarikTunaiData
            } else {
                emptyList()
            }
        }

    tarikTunaiDataList.forEach { (trxDate, nominal) ->
        listDataDonut.add(DataDonutType.DataDonutEntry(trxDate ?: "",nominal?.toInt() ?: 0 ))
    }

    return listDataDonut
}