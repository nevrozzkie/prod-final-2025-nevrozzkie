import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import java.io.ByteArrayOutputStream
import kotlin.math.pow


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
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault()).date
}
