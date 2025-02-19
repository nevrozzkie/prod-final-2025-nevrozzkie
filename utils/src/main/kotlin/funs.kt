import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow

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
fun Long?.isValid(negativePossible: Boolean) = this?.let { if (!negativePossible) this > 0 else abs(this) > 0 } == true
fun String?.isValidRuDate(today: LocalDate?): Boolean {
    return this != null && (this.length == 10 && this.count { it == '.' } == 2) &&
            try {
                val date = this.parseToLocalDate(rusFormat)
                today == null || date > today
            } catch (_: Throwable) {
                false
            }
}


fun getCurrentLocalDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

val rusFormat = LocalDate.Format {
    dayOfMonth()
    char('.')
    monthNumber()
    char('.')
    year()
}
val dbFormat = LocalDate.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
}



fun String.parseToLocalDateSafely(format: DateTimeFormat<LocalDate>) = try {
    LocalDate.parse(this, format)
} catch (_: Throwable) { null }

fun String.parseToLocalDate(format: DateTimeFormat<LocalDate>) =
    LocalDate.parse(this, format)


// https://www.iditect.com/program-example/android--format-number-using-decimal-format-in-kotlin.html
fun Float.formatLikeAmount(withSpaces: Boolean = true): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = if (withSpaces) ' ' else ','
    }
    val decimalFormat = DecimalFormat("#,###.##", symbols)
    val formattedNumber = decimalFormat.format(this)
    return formattedNumber
}

fun String.formatLikeAmount(withSpaces: Boolean = true) = this.reversed()
    .chunked(3)
    .joinToString(if (withSpaces) " " else ",")
    .reversed()
    .trim()

fun Long.formatLikeAmount(withSpaces: Boolean = true) = this.toString().formatLikeAmount(withSpaces)

fun Float.roundTo(numFractionDigits: Int): Float {
    val divider = 10f.pow(numFractionDigits)
    return (Math.round(this * divider) / divider)
}

suspend fun <T> withDatabaseContext(block: suspend () -> T): T {
    return withIOContext(block)
}

suspend fun <T> withIOContext(block: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
        block()
    }
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun LocalDate.toTimestamp(): Long {
    return this.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant().toEpochMilli()
}

fun Instant.toTimestamp(): Long {
    return this.toEpochMilliseconds()
}

// Convert Long (timestamp) back to LocalDate
fun Long.toLocalDate(): LocalDate {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
}
