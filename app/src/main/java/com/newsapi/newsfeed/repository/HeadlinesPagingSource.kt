package com.newsapi.newsfeed.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.newsapi.newsfeed.BuildConfig
import com.newsapi.newsfeed.helpers.Helper.Companion.API_PAGE_SIZE
import com.newsapi.newsfeed.helpers.Helper.Companion.API_STARTING_PAGE
import com.newsapi.newsfeed.helpers.Helper.Companion.convertStringDateToTimestamp
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.networking.TopHeadlinesPageService
import retrofit2.Response
import kotlin.math.ceil

class HeadlinesPagingSource(private val topHeadlinesPageService:  TopHeadlinesPageService): PagingSource<Int, Article>() {

    private var nextPageNumber: Int = API_STARTING_PAGE

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {

            nextPageNumber = params.key ?: API_STARTING_PAGE

            val response = topHeadlinesPageService
                .fetchTopHeadlines(
                    BuildConfig.API_KEY,
                    BuildConfig.source_id,
                    nextPageNumber,
                    API_PAGE_SIZE)

            if (isErrorResponse(response)) {
                throw Exception("")
            }

            var nextKey: Int? = null
            if (!hasReachedAllPages(response)) {
                nextKey = nextPageNumber + 1
            }

            val list = getListOfHeadlines(response)
            list.sortByDescending {
                convertStringDateToTimestamp(it.publishedAt)
            }

             LoadResult.Page(
                data = list,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun hasReachedAllPages(response: Response<TopHeadlinesPage>): Boolean {
        val body = response.body()
        val totalResults = body?.totalResults
        val listOfHeadlines = body?.articles

        if (body == null || listOfHeadlines.isNullOrEmpty()) {
            return true
        }

        val numberOfNecessaryPages = getNumberOfNecessaryPages(totalResults ?: 0)
        return nextPageNumber > numberOfNecessaryPages
    }

    private fun getNumberOfNecessaryPages(numberOfHeadlinesAvailable: Int): Int {
        val fNumberOfHeadlinesAvailable = numberOfHeadlinesAvailable.toFloat()
        val fPageSize = API_PAGE_SIZE.toFloat()
        val fNecessaryPages: Float = fNumberOfHeadlinesAvailable / fPageSize
        return ceil(fNecessaryPages).toInt()
    }

    private fun getListOfHeadlines(response: Response<TopHeadlinesPage>): MutableList<Article> {
       return if (response.body() != null && response.body()?.articles != null) {
           response.body()?.articles!!
       } else {
           mutableListOf()
       }
    }

    private fun isErrorResponse(response: Response<TopHeadlinesPage>): Boolean {
        return !response.isSuccessful
                || response.errorBody() != null
                || response.body()?.status != "ok"
    }
}