package com.newsapi.newsfeed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.Source
import com.newsapi.newsfeed.model.SourcesList
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.networking.RetrofitInstance
import com.newsapi.newsfeed.networking.TopHeadlinesPageService
import com.newsapi.newsfeed.repository.HeadlinesPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
class HeadlinesPagingSourceTest {

    @Test
    fun `load returns page when on successful load of item keyed data`() = runTest {
        val retrofitInstance = TopHeadlinesPageServiceImpl(
            RetrofitInstance.getInstance()
        )

        val pagingSource = HeadlinesPagingSource(retrofitInstance)

        val expectedResponse = retrofitInstance.fetchTopHeadlines(
            BuildConfig.API_KEY,
            BuildConfig.source_id,
            HeadlinesPagingSource.STARTING_PAGE,
            HeadlinesPagingSource.PAGE_SIZE
        )

        val expected = PagingSource.LoadResult.Page(
            data = expectedResponse.body()!!.articles!!,
            prevKey = null,
            nextKey =  2
        )

        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                placeholdersEnabled = false,
                loadSize = 2
            )
        )

        assertEquals(expected, actual)
    }


    class TopHeadlinesPageServiceImpl
    constructor(private val retrofit: Retrofit) : TopHeadlinesPageService {

        private var successResponse: Response<TopHeadlinesPage>

        init {
            successResponse = Response.success<TopHeadlinesPage>(
                TopHeadlinesPage(
                    mutableListOf<Article>(mockNewArticle(), mockNewArticle()),
                    "ok",
                    2
                )
            )
        }

        private val endpoint by lazy {
            retrofit.create(TopHeadlinesPageService::class.java)
        }

        override suspend fun fetchTopHeadlines(
            apiKey: String,
            sources: String,
            page: Int,
            pageSize: Int
        ): Response<TopHeadlinesPage> {
            return successResponse
        }

        override suspend fun fetchAllSources(apiKey: String): Response<SourcesList> {
            return endpoint.fetchAllSources(apiKey)
        }

        private fun mockNewArticle(): Article {
            return Article(
                "Argaam",
                null,
                "Malicious encryptors for Apple computers could herald new risks for macOS users.",
                "2023-04-18T13:21:16+00:00",
                Source("ars-technica", "Ars Technica"),
                "Apple’s Macs have long escaped ransomware, but that may be changing",
                "https://www.wired.com/story/apple-mac-lockbit-ransomware-samples/",
                "\"https://cdn.arstechnica.net/wp-content/uploads/2023/04/macbook-pink-760x380.jpg"
            )
        }

    }
}