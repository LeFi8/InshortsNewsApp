package com.example.inshortsnewsapp.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class DateFormatter {
    companion object {
        private const val dateFormat = "EEEE dd/MM/yyyy"
        fun formatDate(dateStr: String): String {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            try {
                val date = inputDateFormat.parse(dateStr)
                return outputDateFormat.format(date!!)
            } catch (e: ParseException) {
                println(e.message)
            }
            return dateStr
        }
    }
}