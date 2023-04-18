package com.newsapi.newsfeed.networking

import com.newsapi.newsfeed.model.TopHeadlinesPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TopHeadlinesPageService {
    @GET("top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<TopHeadlinesPage>
}