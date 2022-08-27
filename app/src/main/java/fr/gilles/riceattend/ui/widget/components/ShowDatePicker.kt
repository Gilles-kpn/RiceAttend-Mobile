package fr.gilles.riceattend.ui.widget.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.ZoneId
import java.util.*

@Composable
fun ShowDatePicker(
    onDateSelected: (Instant) -> Unit = {}
) {
    val context = LocalContext.current
    val year: Int
    val month: Int
    val day: Int
    val mHour: Int
    val mMinute: Int

    // Initializing a Calendar
    val calendar = Calendar.getInstance()

    // Fetching current year, month and day
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    mHour = calendar.get(Calendar.HOUR)
    mMinute = calendar.get(Calendar.MINUTE)

    calendar.time = Date()

    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }


    val timePicker = TimePickerDialog(
        context,
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            //convert hourOfDay and minute to 2 digits
            val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
            val min = if (minute < 10) "0$minute" else minute.toString()
            time = "$hour:$min"
            parseDateFromString("$date $time")?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    onDateSelected(it.atZone(ZoneId.systemDefault()).toInstant())
                }
            }
        },
        mHour,
        mMinute,
        true
    )


    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            //convert mDayMonth and mMont to 2 digits
            val pickerMonth = if (mMonth < 10) "0${mMonth + 1}" else mMonth.toString()
            val pickerDay = if (mDayOfMonth < 10) "0$mDayOfMonth" else mDayOfMonth.toString()
            date = "$pickerDay/${pickerMonth}/$mYear"
            timePicker.show()
        }, year, month, day
    )
    datePicker.datePicker.minDate = calendar.timeInMillis
    datePicker.show()

}