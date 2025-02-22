package utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.pow

// https://www.iditect.com/program-example/android--format-number-using-decimal-format-in-kotlin.html
fun Float.formatLikeAmount(withSpaces: Boolean = true): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = if (withSpaces) ' ' else ','
    }
    val decimalFormat = DecimalFormat("#,###.##", symbols)
    val formattedNumber = decimalFormat.format(this)
    return formattedNumber
}

fun String.formatLikeAmount(withSpaces: Boolean = true, addRuble: Boolean = false): String {
    return if (this.isNotEmpty()) {
        val isMinus = this[0] == '-'
        val text = this.removePrefix("-").reversed()
            .chunked(3)
            .joinToString(if (withSpaces) " " else ",")
            .reversed()
            .trim()
        "${if(isMinus)"-" else ""}$text${if (addRuble) " ₽" else ""}"
    } else ""
}



fun Long.formatLikeAmount(withSpaces: Boolean = true, addRuble: Boolean = false) = this.toString().formatLikeAmount(withSpaces, addRuble)

fun Float.roundTo(numFractionDigits: Int): Float {
    val divider = 10f.pow(numFractionDigits)
    return (Math.round(this * divider) / divider)
}