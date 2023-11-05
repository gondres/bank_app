package com.phincon.qris.model


import com.google.gson.annotations.SerializedName


data class HistoryChartModel(
    @SerializedName("type")
    val type: String,
    @SerializedName("data")
    val `data`: Any
) {
    fun getDataDonutType(): DataDonutType? {
        if (type == "donutChart" && data is List<*>) {
            val dataList = data as List<*>
            if (dataList.isNotEmpty() && dataList[0] is Map<*, *>) {
                val dataMap = dataList[0] as Map<*, *>
                val label = dataMap["label"] as String
                val percentage = dataMap["percentage"] as String
                val dataEntries = dataMap["data"] as List<*>
                val donutDataEntries = dataEntries.map {
                    if (it is Map<*, *>) {
                        val trxDate = it["trx_date"] as String
                        val nominal = (it["nominal"] as Double).toInt()
                        DataDonutType.DataDonutEntry(trxDate, nominal)
                    } else {
                        null
                    }
                }
                return DataDonutType(label, percentage, donutDataEntries.filterNotNull())
            }
        }
        return null
    }
}

data class DataDonutType(
    @SerializedName("label")
    val label: String,
    @SerializedName("percentage")
    val percentage: String,
    @SerializedName("data")
    val `data`: List<DataDonutEntry>
) {
    data class DataDonutEntry(
        @SerializedName("trx_date")
        val trxDate: String,
        @SerializedName("nominal")
        val nominal: Int
    )
}
