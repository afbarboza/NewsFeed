package com.newsapi.newsfeed.model

data class TopHeadlinesPage (
    val articles: MutableList<Article>?,
    val status: String,
    val totalResults: Int
)