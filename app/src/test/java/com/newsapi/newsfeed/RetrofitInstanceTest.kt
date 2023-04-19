package com.newsapi.newsfeed

import com.newsapi.newsfeed.model.SourcesList
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.networking.RetrofitInstance
import com.newsapi.newsfeed.networking.TopHeadlinesPageService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

class RetrofitInstanceTest {

    private val REQUEST_HAS_SUCCEEDED = 200

    @Test
    fun `when call getInstance, check if it is the expected base URL`() {
        val expectedBaseUrl = "https://newsapi.org/v2/"
        val instance: Retrofit = RetrofitInstance.getInstance()
        assert(instance.baseUrl().toUrl().toString() == expectedBaseUrl)
    }

     @Test
     fun `when call fetchAllSources with a successfull response, check for response body`() {
        val service = TopHeadlinesPageServiceImpl(RetrofitInstance.getInstance())
        runBlocking {
            val response = service.fetchAllSources(BuildConfig.API_KEY)

            val successBody = response.body()
            val isSuccessfull = response.isSuccessful

            if (isSuccessfull && response.code() == REQUEST_HAS_SUCCEEDED) {
                assert(successBody != null)
                val sourcesList = successBody?.sources
                assert(sourcesList?.get(0)?.id  == "ars-technica")
            } else {
                /* In this unit test, we do not test error scenarios. Check next unit test for that */
                assert(true)
            }
        }
    }

    @Test
    fun `when call fetchAllSources with a error response, check for error body`() {
        val service = TopHeadlinesPageServiceImpl(RetrofitInstance.getInstance())
        runBlocking {
            val response = service.fetchAllSources(BuildConfig.API_KEY)
            val isSuccessfull = response.isSuccessful
            val errorBody = response.errorBody()

            if (!isSuccessfull) {
                assert(errorBody != null)
                assert(response.code() != REQUEST_HAS_SUCCEEDED)
            } else {
                /* In this unit test, we do not test successfull scenarios. Check previous unit test for that */
                assert(true)
            }
        }
    }

    /**
     * This is a helper class to make possible test RetrofitInstance and also
     * TopHeadlinesPageService. This class should not be used outside testing environment.
     *
     * @param retrofit: Retrofit - retrofit singleton instance.
     *
     * For more information, please refer to:
     * https://rommansabbir.com/android-network-calling-with-retrofit-and-unit-testing
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

}