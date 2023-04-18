package com.newsapi.newsfeed.repository

import com.newsapi.newsfeed.BuildConfig
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.networking.RetrofitInstance
import com.newsapi.newsfeed.networking.TopHeadlinesPageService
import retrofit2.Response

class TopHeadlinesPageRepository {

    private var topHeadlinesPageService:  TopHeadlinesPageService = RetrofitInstance
        .getInstance()
        .create(TopHeadlinesPageService::class.java)

    fun getHeadlinesPagingSource() = HeadlinesPagingSource(topHeadlinesPageService)

    suspend fun getAllSources() = topHeadlinesPageService.fetchAllSources(BuildConfig.API_KEY)
}