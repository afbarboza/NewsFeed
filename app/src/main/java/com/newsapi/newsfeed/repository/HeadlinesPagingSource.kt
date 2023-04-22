package com.newsapi.newsfeed.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.newsapi.newsfeed.BuildConfig
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.networking.RetrofitInstance
import com.newsapi.newsfeed.networking.TopHeadlinesPageService
import kotlinx.coroutines.delay
import retrofit2.Response
import kotlin.math.ceil

class HeadlinesPagingSource(private val topHeadlinesPageService:  TopHeadlinesPageService): PagingSource<Int, Article>() {

    /**
     * The News Api paging starts at 1 (a page of value 0 will return the same as 1)
     */
    companion object {
        val PAGE_SIZE = 3
        val STARTING_PAGE = 1
    }

    private var nextPageNumber: Int = STARTING_PAGE

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {

            nextPageNumber = params.key ?: STARTING_PAGE

            if (nextPageNumber != STARTING_PAGE) {
                delay(3_000L)
            }

            val response = topHeadlinesPageService
                .fetchTopHeadlines(
                    BuildConfig.API_KEY,
                    BuildConfig.source_id,
                    nextPageNumber,
                    PAGE_SIZE)

            var nextKey: Int? = null
            if (!hasReachedEndOfList(response)) {
                nextKey = nextPageNumber + 1
            }

            /* TODO handle empty body and empty list of articles */
             LoadResult.Page(
                data = response.body()!!.articles!!,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun hasReachedEndOfList(response: Response<TopHeadlinesPage>): Boolean {
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
        val fPageSize = PAGE_SIZE.toFloat()
        val fNecessaryPages: Float = fNumberOfHeadlinesAvailable / fPageSize
        return ceil(fNecessaryPages).toInt()
    }

}