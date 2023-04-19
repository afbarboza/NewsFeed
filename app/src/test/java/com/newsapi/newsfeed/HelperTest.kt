package com.newsapi.newsfeed

import com.newsapi.newsfeed.helpers.Helper.Companion.formatDate
import org.junit.Test

class HelperTest {
    @Test
    fun `when call formatDate with empty string, return empty string`() {
        val formattedDate = formatDate("")
        assert(formattedDate.isEmpty())
    }

    @Test
    fun `when call formateDate with invalid content, return empty string`() {
        val formattedDate = formatDate("Donald Ervin Knuth")
        assert(formattedDate.isEmpty())
    }

    @Test
    fun `when call formateDate with invalid date string format, return empty string`() {
        val formattedDate = formatDate("1970-01-01")
        assert(formattedDate.isEmpty())
    }

    @Test
    fun `when call formatDate with valid date string format, return formated string`() {
        val formattedDate1 = formatDate("2023-04-18T13:21:16+00:00")
        val formattedDate2 = formatDate("2023-04-18T16:07:21.7657117Z")

        assert(!formattedDate1.isNullOrEmpty() && formattedDate1 == "18/4/2023 13:21")
        assert(!formattedDate2.isNullOrEmpty() && formattedDate2 == "18/4/2023 16:7")

    }

}