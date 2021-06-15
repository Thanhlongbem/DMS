@file:Suppress("INACCESSIBLE_TYPE")

package com.ziperp.dms.extensions

import android.annotation.SuppressLint
import com.ziperp.dms.AppConfiguration
import java.text.SimpleDateFormat
import java.util.*

fun Date.dateFrom(day: Int, month: Int, year: Int): Date {
    return GregorianCalendar(year, month, day).time
}

fun Date.day(): Int{
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_MONTH)
}

fun Date.month(): Int{
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.MONTH)
}

fun Date.yearValue(): Int{
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.YEAR)
}

@SuppressLint("SimpleDateFormat")
fun Date.toString(format: String): String{
    return SimpleDateFormat(format).format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.toDotString(): String{
    return SimpleDateFormat("dd.MM.yyyy").format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.fromISO(iso: String): Date{
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(iso)!!
}

@SuppressLint("SimpleDateFormat")
fun Date.toISO(): String{
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(this)
}

fun Date.dateAfter(shift: Int): Date{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, shift)
    return calendar.time
}

fun Date.dateBefore(shift: Int): Date{
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -shift)
    return calendar.time
}

fun Date.isWeekendDate(): Boolean {
    val c = Calendar.getInstance()
    c.time = this
    val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
    return dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY
}
//Handling date to string

fun Date.toDateString(format: String = AppConfiguration.SERVER_DATE_FORMAT): String{ //Server response
    return SimpleDateFormat(format, Locale.US).format(this)
}

fun Date.toDateTimeString(format: String = AppConfiguration.SERVER_DATE_TIME_FORMAT): String{//Server response
    return SimpleDateFormat(format, Locale.US).format(this)
}

fun Date.toHourMinuteSecond(format: String = AppConfiguration.SERVER_HOUR_MINUTE_FORMAT): String{//Server response
    return SimpleDateFormat(format, Locale.US).format(this)
}

fun Date.toDateTimeFullString(format: String = AppConfiguration.SERVER_DATE_TIME_FULL_FORMAT): String{//Server response
    return SimpleDateFormat(format, Locale.US).format(this)
}

fun Date.toSlashDateString(format: String = AppConfiguration.APP_DATE_FORMAT): String{
    return SimpleDateFormat(format, Locale.US).format(this)
}

fun Date.toSlashDateTimeString(format: String =  AppConfiguration.APP_DATE_TIME_FORMAT): String{
    return SimpleDateFormat(format, Locale.US).format(this)
}

//String to date
fun String.toDate(format: String = AppConfiguration.SERVER_DATE_FORMAT): Date?{//Server response
    return try { SimpleDateFormat(format, Locale.US).parse(this) } catch (e: Exception) { null }
}

fun String.toDateTimeFull(format: String = AppConfiguration.SERVER_DATE_TIME_FULL_FORMAT): Date?{//Server response
    return try { SimpleDateFormat(format, Locale.US).parse(this) } catch (e: Exception) { null }
}

fun String.slashToDate(format: String = AppConfiguration.APP_DATE_FORMAT): Date?{
    return try { SimpleDateFormat(format, Locale.US).parse(this) } catch (e: Exception) { null }
}

fun String.toDateTime(format: String = AppConfiguration.SERVER_DATE_TIME_FORMAT): Date?{//Server response
    return try { SimpleDateFormat(format, Locale.US).parse(this) } catch (e: Exception) { null }
}

fun String.slashToDateTime(format: String = AppConfiguration.APP_DATE_TIME_FORMAT): Date?{
    return try { SimpleDateFormat(format, Locale.US).parse(this) } catch (e: Exception) { null }
}

//Show info
fun String.reformatDate(): String {
    var result = this
    if(isNotBlank()){
        toDate()?.let{
            result = it.toSlashDateString()
        }
    }
    return result
}

fun String.reformatDateTime(): String {
    var result = this
    if(isNotBlank()){
        toDateTime()?.let{
            result = it.toSlashDateTimeString()
        }
    }
    return result
}

//Date for request api
fun String.convertDate(): String {
    var result = this
    if(isNotBlank()){
        slashToDate()?.let{
            result = it.toDateString()
        }
    }
    return result
}

fun String.convertDateTime(): String {
    var result = this
    if(isNotBlank()){
        slashToDateTime()?.let{
            result = it.toDateTimeString()
        }
    }
    return result
}