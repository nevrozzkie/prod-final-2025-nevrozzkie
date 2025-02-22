package utils

import kotlinx.datetime.LocalDate
import kotlin.math.abs

fun formatWithoutDotsToDate(input: String): String {
    val digitsOnly = input.replace(Regex("\\D"), "")
    val day = digitsOnly.take(2)
    val month = digitsOnly.drop(2).take(2)
    val year = digitsOnly.drop(4).take(4)

    return buildString {
        if (day.isNotEmpty()) append(day)
        if (month.isNotEmpty()) append(".$month")
        if (year.isNotEmpty()) append(".$year")
    }
}


fun String?.isValid() = this?.isNotBlank() == true
fun Long?.isValid(negativePossible: Boolean) =
    this?.let { if (!negativePossible) this > 0 else abs(this) > 0 } == true

fun String?.isValidRuDate(today: LocalDate?): Boolean {
    return this != null && (this.length == 10 && this.count { it == '.' } == 2) &&
            try {
                val date = this.parseToLocalDate(rusFormat)
                today == null || date > today
            } catch (_: Throwable) {
                false
            }
}