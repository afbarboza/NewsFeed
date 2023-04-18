package com.newsapi.newsfeed.repository

import com.newsapi.newsfeed.BuildConfig
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.networking.RetrofitInstance
import com.newsapi.newsfeed.networking.TopHeadlinesPageService
import retrofit2.Response

class TopHeadlinesPageRepository {
    fun getHeadlinesPagingSource() = HeadlinesPagingSource()
}