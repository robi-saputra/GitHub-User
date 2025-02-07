package robi.technicaltest.baseapplication.utils

import android.annotation.SuppressLint
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun dateNow(outputPattern: String, locale: Locale): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(outputPattern, locale)
    return dateFormat.format(calendar.time)
}

@SuppressLint("NewApi")
fun String?.formatDate(inputPattern: String, outputPattern: String, locale: Locale): String {
    if (this!=null) {
        val formatter = DateTimeFormatter.ofPattern(inputPattern, locale)
        val parsedDateTime = ZonedDateTime.parse(this, formatter)
        val outputFormatter = DateTimeFormatter.ofPattern(outputPattern, locale)
        return parsedDateTime.format(outputFormatter)
    } else {
        return ""
    }
}

@SuppressLint("NewApi")
fun String?.timeElapsed(inputPattern: String, locale: Locale): String {
    if (this!=null) {
        val formatter = DateTimeFormatter.ofPattern(inputPattern, locale)
        val parsedDateTime = ZonedDateTime.parse(this, formatter)
        val now = ZonedDateTime.now()
        val years = ChronoUnit.YEARS.between(parsedDateTime, now)
        val months = ChronoUnit.MONTHS.between(parsedDateTime.plusYears(years), now)
        val days = ChronoUnit.DAYS.between(parsedDateTime.plusYears(years).plusMonths(months), now)
        val hours = ChronoUnit.HOURS.between(
            parsedDateTime.plusYears(years).plusMonths(months).plusDays(days), now
        )
        val minutes = ChronoUnit.MINUTES.between(
            parsedDateTime.plusYears(years).plusMonths(months).plusDays(days).plusHours(hours), now
        )

        return buildString {
            if (years > 0) append("$years year${if (years > 1) "s" else ""} ")
            if (months > 0) append("$months month${if (months > 1) "s" else ""} ")
            if (days > 0) append("$days day${if (days > 1) "s" else ""} ")
            if (hours > 0) append("$hours hour${if (hours > 1) "s" else ""} ")
            if (minutes > 0) append("$minutes minute${if (minutes > 1) "s" else ""}")
            if (isBlank()) append("just now")
        }.trim()
    } else {
        return ""
    }
}