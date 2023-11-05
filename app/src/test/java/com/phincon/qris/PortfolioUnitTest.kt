package com.phincon.qris

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.phincon.qris.database.AppDatabase
import com.phincon.qris.utils.MainDispatcherRule
import com.phincon.qris.database.DatabaseStateEvent
import com.phincon.qris.database.dao.UserDao
import com.phincon.qris.database.entity.HistoryTransaction
import com.phincon.qris.database.entity.User
import com.phincon.qris.database.repository.HistoryRepository
import com.phincon.qris.database.repository.UserRepository
import com.phincon.qris.model.DataDonutType
import com.phincon.qris.model.HistoryChartModel
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.screen.payment.vm.PaymentViewModel
import com.phincon.qris.utils.getOrAwaitValue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.Executors
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class PortfolioUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var historyRepository: HistoryRepository
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var paymentViewModel: PaymentViewModel

    val name = "test"

    val dataTarikTunai = listOf(
        DataDonutType.DataDonutEntry(
            nominal = 1000000,
            trxDate = "21/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 500000,
            trxDate = "20/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 1000000,
            trxDate = "19/01/2023"
        )
    )

    val dataQrisPayment = listOf(
        DataDonutType.DataDonutEntry(
            nominal = 159000,
            trxDate = "21/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 35000,
            trxDate = "20/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 1500,
            trxDate = "19/01/2023"
        )
    )

    val dataGoPay = listOf(
        DataDonutType.DataDonutEntry(
            nominal = 200000,
            trxDate = "21/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 195000,
            trxDate = "20/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 5000000,
            trxDate = "19/01/2023"
        )
    )

    val dataLainnya = listOf(
        DataDonutType.DataDonutEntry(
            nominal = 1000000,
            trxDate = "21/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 500000,
            trxDate = "20/01/2023"
        ),
        DataDonutType.DataDonutEntry(
            nominal = 1000000,
            trxDate = "19/01/2023"
        )
    )

    @Before
    fun setup(){
    }

    @Test
    fun `test get data donutChart Tarik Tunai`(){
        assertEquals(dataTarikTunai,getDataByTpe("Tarik Tunai"))
    }

    @Test
    fun `test get data donutChart QRIS Payment`(){
        assertEquals(dataQrisPayment,getDataByTpe("QRIS Payment"))
    }

    @Test
    fun `test get data donutChart Topup Gopay`(){
        assertEquals(dataGoPay,getDataByTpe("Topup Gopay"))
    }

    @Test
    fun `test get data donutChart Lainnya`(){
        assertEquals(dataLainnya,getDataByTpe("Lainnya"))
    }

    fun getDonutChartData(): List<HistoryChartModel> {
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
        val historyChartModels: List<HistoryChartModel> = chartDataList
        val donutChartDataList = historyChartModels.filter { it.type == "donutChart" }

        return donutChartDataList
    }

    fun getDataByTpe(label: String) : List<DataDonutType.DataDonutEntry>{
        var listDataDonut  = mutableListOf<DataDonutType.DataDonutEntry>()
        val historyChartModels: List<HistoryChartModel> = com.phincon.qris.utils.getDonutChartData()
        val tarikTunaiDataList = historyChartModels
            .filter { it.type == "donutChart" } // Filter donutChart dataTarikTunais
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

}