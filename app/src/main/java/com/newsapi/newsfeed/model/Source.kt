package com.newsapi.newsfeed.model

import org.intellij.lang.annotations.Language

data class Source(
    val id: String,
    val name: String,
    val description: String? = "",
    val url: String? = "",
    val category: String? = "",
    val language: String? = "",
    val country: String? = ""
)