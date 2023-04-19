package com.newsapi.newsfeed.helpers

import android.icu.util.Calendar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Helper {
    companion object {
        fun formatDate(date: String): String {
            try {
                val sanitizedDate = date.substring(0, 16)
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm")
                val date: LocalDate = LocalDate.parse(sanitizedDate, format)
                return "${date.dayOfMonth}/${date.monthValue}/${date.year}"
            } catch (e: java.lang.Exception) {
                return ""
            }
        }
    }
}