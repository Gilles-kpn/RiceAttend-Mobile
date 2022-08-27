package fr.gilles.riceattend.ui.widget.components

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun parseDateFromString(value: String): LocalDateTime? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    }
    return null
}

fun formatDateToHumanReadable(date: Date): String {
    return SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(date)
}