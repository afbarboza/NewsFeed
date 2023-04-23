package com.newsapi.newsfeed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.newsapi.newsfeed.TestHelper.mockNewArticle
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.view.DetailsActivity
import com.newsapi.newsfeed.viewmodel.HeadlineDetailsViewModel
import com.newsapi.newsfeed.viewmodel.HeadlineDetailsViewModelFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HeadlineDetailsViewModelTest {

    private lateinit var article: Article
    private lateinit var activity: DetailsActivity
    private lateinit var headlineDetailsViewModel: HeadlineDetailsViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        article = mockNewArticle()
        headlineDetailsViewModel = HeadlineDetailsViewModel(article)

        activity = Robolectric
            .buildActivity(DetailsActivity::class.java)
            .get()
    }

    @Test
    fun `when pass argument to view model constructor, check if live data was updated`() {
        assertEquals(article, headlineDetailsViewModel.articleData.value)
    }

    @Test
    fun `when instantiate view model using view model provider, check if live data was updated`() {
        article = mockNewArticle()
        val viewModelFactory = HeadlineDetailsViewModelFactory(article)
        headlineDetailsViewModel = ViewModelProvider(activity, viewModelFactory)
            .get(HeadlineDetailsViewModel::class.java)

        assertEquals(article, headlineDetailsViewModel.articleData.value)
    }
}