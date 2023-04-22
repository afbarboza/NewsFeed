package com.newsapi.newsfeed.helpers

import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class Helper {
    companion object {

        const val ARTICLE_PARAM = "article"
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm"
        private const val DATE_START = 0
        private const val DATE_END = 16

        fun formatDate(dateStr: String): String {
            return try {
                val dateTime = convertStringToLocalDateTime(dateStr)
                "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year} ${dateTime.hour}:${dateTime.minute}"
            } catch (e: java.lang.Exception) {
                ""
            }
        }

        fun convertStringDateToTimestamp(dateStr: String): Long {
            return try {
                val dateTime = convertStringToLocalDateTime(dateStr)
                dateTime
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli()
            } catch (e: java.lang.Exception) {
                -1
            }
        }

        private fun convertStringToLocalDateTime(dateStr: String): LocalDateTime {
            val sanitizedDate = dateStr.substring(DATE_START, DATE_END)
            val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
            return LocalDateTime.parse(sanitizedDate, formatter)
        }

    }

}