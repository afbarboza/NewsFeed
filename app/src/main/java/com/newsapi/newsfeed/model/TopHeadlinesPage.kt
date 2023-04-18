package com.newsapi.newsfeed.model

data class TopHeadlinesPage (
    val articles: List<Article>?,
    val status: String,
    val totalResults: Int
)