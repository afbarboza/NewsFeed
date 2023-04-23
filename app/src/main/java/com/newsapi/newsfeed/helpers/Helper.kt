package com.newsapi.newsfeed.helpers


import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Helper {
    companion object {

        const val ARTICLE_PARAM = "article"
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm"
        private const val DATE_START = 0
        private const val DATE_END = 16

        const val API_PAGE_SIZE = 10
        const val API_STARTING_PAGE = 1

        const val INVALID_TIMESTAMP: Long = -1

        @Suppress("LongMethod")
        fun formatDate(dateStr: String): String {
            return try {
                val dateTime = convertStringToLocalDateTime(dateStr)
                "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year} ${dateTime.hour}:${dateTime.minute}"
            } catch (e: java.lang.Exception) {
                ""
            }
        }

        @Suppress("LongMethod")
        fun convertStringDateToTimestamp(dateStr: String): Long {
            return try {
                val dateTime = convertStringToLocalDateTime(dateStr)
                dateTime
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli()
            } catch (e: java.lang.Exception) {
                INVALID_TIMESTAMP
            }
        }

        private fun convertStringToLocalDateTime(dateStr: String): LocalDateTime {
            val sanitizedDate = dateStr.substring(DATE_START, DATE_END)
            val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
            return LocalDateTime.parse(sanitizedDate, formatter)
        }

    }

}