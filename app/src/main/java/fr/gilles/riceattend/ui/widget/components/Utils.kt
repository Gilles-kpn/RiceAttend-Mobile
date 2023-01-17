package fr.gilles.riceattend.ui.widget.components

import android.os.Build
import fr.gilles.riceattend.services.storage.SessionManager
import java.math.BigInteger
import java.security.MessageDigest
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

fun formatDate(date :Date):String{
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
}
fun formatTime(date :Date):String{
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
}
fun md5(input:String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
fun userGravatar(email:String = if(SessionManager.session.user != null) SessionManager.session.user!!.email else ""): String {
    return "https://www.gravatar.com/avatar/"+md5(email)
}
