package com.newsapi.newsfeed

import com.newsapi.newsfeed.TestHelper.mockNewArticle
import com.newsapi.newsfeed.TestHelper.mockNewSource
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.Source
import com.newsapi.newsfeed.model.SourcesList
import com.newsapi.newsfeed.model.TopHeadlinesPage
import org.junit.Test

class ModelTest {
    @Test
    fun articleCreation_returnsNewArticle() {
        val article: Article? = mockNewArticle()
        assert(article != null)
    }

    @Test
    fun sourceCreation_returnsNewSource() {
        val source: Source? = mockNewSource()
        assert(source != null)
    }

    @Test
    fun sourcesListCreation_returnsNewSourcesList() {
        val list = listOf<Source>(mockNewSource(), mockNewSource(), mockNewSource())
        val sourceList = SourcesList(list, "ok")
        assert(sourceList != null && sourceList.sources.size == 3)
    }

    @Test
    fun topHeadlinesPage_returnsNewTopHeadlinesPage() {
        val list = mutableListOf<Article>(mockNewArticle(), mockNewArticle(), mockNewArticle())
        val topHeadlinesPage = TopHeadlinesPage(list, "ok", 10)
        assert(topHeadlinesPage.articles?.size == 3)
    }
}