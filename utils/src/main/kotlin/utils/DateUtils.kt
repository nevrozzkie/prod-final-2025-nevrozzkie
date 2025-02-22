package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime

fun getCurrentLocalDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

val rusFormat = LocalDate.Format {
    dayOfMonth()
    char('.')
    monthNumber()
    char('.')
    year()
}
val time24Format = LocalTime.Format {
    this.hour()
    char(':')
    this.minute()
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
} catch (_: Throwable) {
    null
}

fun String.parseToLocalDate(format: DateTimeFormat<LocalDate>) =
    LocalDate.parse(this, format)

fun LocalDate.toTimestamp(): Long {
    return this.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant().toEpochMilli()
}

fun Instant.toTimestamp(): Long {
    return this.toEpochMilliseconds()
}

fun Long.toLocalDate(): LocalDate {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
}
fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
}