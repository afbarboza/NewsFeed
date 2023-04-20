package com.newsapi.newsfeed

import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.Source
import com.newsapi.newsfeed.model.SourcesList
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.networking.RetrofitInstance
import com.newsapi.newsfeed.networking.TopHeadlinesPageService
import com.newsapi.newsfeed.repository.HeadlinesPagingSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

/**
 *  RetrofitInstanceTest - test suite to exercise methods available both:
 *  @param RetrofitInstance
 *  @param TopHeadlinesPageService
 *
 *  Please, be aware that this class doest not mock any data as it perform
 *  real API endpoint calls. Therefore, it relies on internet connectivity.
 */
class RetrofitInstanceTest {

    private lateinit var service: TopHeadlinesPageServiceImpl

    @Before
    fun setUpTestingEnvironment() {
        this.service = TopHeadlinesPageServiceImpl(RetrofitInstance.getInstance())
    }

    @Test
    fun `when call getInstance, check if it is the expected base URL`() {
        val expectedBaseUrl = "https://newsapi.org/v2/"
        val instance: Retrofit = RetrofitInstance.getInstance()
        assert(instance.baseUrl().toUrl().toString() == expectedBaseUrl)
    }

     @Test
     fun `when call fetchAllSources with a successfull response, check for response body`() {
        runBlocking {
            val response = service.fetchAllSources(BuildConfig.API_KEY)

            val successBody = response.body()
            val isSuccessfull = response.isSuccessful

            if (isSuccessfull) {
                assert(successBody != null)
                assert(isSourceCorrect(successBody?.sources))
            } else {
                /* In this unit test, in case of a error response coming from API,
                 * we do not test error scenarios.
                 * Check next unit test for that */
                assert(true)
            }
        }
    }

    @Test
    fun `when call fetchAllSources with a error response, check for error body`() {
        runBlocking {
            val response = service.fetchAllSources(BuildConfig.API_KEY)

            val isSuccessfull = response.isSuccessful
            val errorBody = response.errorBody()

            if (!isSuccessfull) {
                assert(errorBody != null)
            } else {
                /* In this unit test, in case of a successfull request coming from API,
                 * we do not test successfull scenarios.
                 * Check previous unit test for that.
                 */
                assert(true)
            }
        }
    }

    @Test
    fun `when call fetchTopHeadlines for a given source, check if headline matches the source`() {
        runBlocking {
            val response = service.fetchTopHeadlines(
                BuildConfig.API_KEY,
                BuildConfig.source_id,
                HeadlinesPagingSource.STARTING_PAGE,
                HeadlinesPagingSource.PAGE_SIZE
            )

            val isSuccessfull = response.isSuccessful
            val topHeadlinesPage: TopHeadlinesPage? = response.body()

            if (isSuccessfull) {
                if (isEmptyListOfHeadlines(topHeadlinesPage)) {
                    assert(true)
                } else {
                    val firstHeadline: Article? = topHeadlinesPage?.articles?.get(0)
                    assert(isHeadlineMatchingSource(firstHeadline))
                }
            } else {
                /* In this unit test, in case of a error response coming from API,
                 *  we do not test error scenarios.
                 */
                assert(true)
            }

        }
    }

    /**
     * This is a helper class to make possible test RetrofitInstance and also
     * TopHeadlinesPageService. This class *should not* be used outside testing environment.
     *
     * @param retrofit: Retrofit - retrofit singleton instance.
     *
     * For more information, please refer to:
     * @see https://rommansabbir.com/android-network-calling-with-retrofit-and-unit-testing
     */
    private inner class TopHeadlinesPageServiceImpl
    constructor(private val retrofit: Retrofit) : TopHeadlinesPageService {

        private val endpoint by lazy {
            retrofit.create(TopHeadlinesPageService::class.java)
        }

        override suspend fun fetchTopHeadlines(
            apiKey: String,
            sources: String,
            page: Int,
            pageSize: Int
        ): Response<TopHeadlinesPage> {
            return endpoint.fetchTopHeadlines(apiKey, sources, page, pageSize)
        }

        override suspend fun fetchAllSources(apiKey: String): Response<SourcesList> {
            return endpoint.fetchAllSources(apiKey)
        }

    }

    /**
     * According to the API, the first source returned by @GET("sources") is ABC News
     */
    private fun isSourceCorrect(sourcesList: List<Source>?) =
        !sourcesList.isNullOrEmpty() &&
        sourcesList?.get(0)?.id  == "abc-news"


    private fun isEmptyListOfHeadlines(topHeadlinesPage: TopHeadlinesPage?) =
        topHeadlinesPage == null ||
        topHeadlinesPage.articles.isNullOrEmpty()

    private fun isHeadlineMatchingSource(headline: Article?): Boolean {
        if (headline == null) {
            return false
        }

        return headline.source?.id == BuildConfig.source_id
    }
}