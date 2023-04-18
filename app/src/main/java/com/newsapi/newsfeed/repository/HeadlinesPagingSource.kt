package com.newsapi.newsfeed.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.newsapi.newsfeed.BuildConfig
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.networking.RetrofitInstance
import com.newsapi.newsfeed.networking.TopHeadlinesPageService

class HeadlinesPagingSource: PagingSource<Int, Article>() {

    /**
     * The News Api paging starts at 1 (a page of value 0 will return the same as 1)
     */
    val PAGE_SIZE = 5
    private val STARTING_PAGE = 1

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {

            val nextPageNumber = params.key ?: STARTING_PAGE

            val response = topHeadlinesPageService
                .fetchTopHeadlines(
                    BuildConfig.API_KEY,
                    BuildConfig.source_id,
                    nextPageNumber,
                    PAGE_SIZE)

            /* TODO handle empty body and empty list of articles */
             LoadResult.Page(
                data = response.body()!!.articles!!,
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    private var topHeadlinesPageService = RetrofitInstance
        .getInstance()
        .create(TopHeadlinesPageService::class.java)

}