package com.newsapi.newsfeed.helpers

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Helper {
    companion object {

        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm"
        private const val DATE_START = 0
        private const val DATE_END = 16
        const val ARTICLE_PARAM = "article"

        fun formatDate(dateStr: String): String {
            try {
                val sanitizedDate = dateStr.substring(DATE_START, DATE_END)
                val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
                val dateTime = LocalDateTime.parse(sanitizedDate, formatter)
                return "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year} ${dateTime.hour}:${dateTime.minute}"
            } catch (e: java.lang.Exception) {
                return ""
            }
        }
    }
}