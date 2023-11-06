package com.phincon.qris.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

fun convertToDDMMYYYY(dateTimeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat = SimpleDateFormat("dd/MM/yyyy")
    val date: Date = inputFormat.parse(dateTimeString)
    outputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set the desired timezone

    return outputFormat.format(date)
}