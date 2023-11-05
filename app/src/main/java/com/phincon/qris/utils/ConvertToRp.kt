package com.phincon.qris.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun formatToRupiah(value: Int): String {
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = '.'
    val df = DecimalFormat("#,###", symbols)
    return "Rp " + df.format(value)
}
