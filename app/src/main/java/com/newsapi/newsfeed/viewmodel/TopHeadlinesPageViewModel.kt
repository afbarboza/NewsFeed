package com.newsapi.newsfeed.viewmodel

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.model.TopHeadlinesPage
import com.newsapi.newsfeed.repository.TopHeadlinesPageRepository

class TopHeadlinesPageViewModel(
    private val repository: TopHeadlinesPageRepository
)
    : ViewModel() {
        val items: LiveData<PagingData<Article>> = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { repository.getHeadlinesPagingSource() }
        )
            .liveData


    fun getHeadlinesPagingSource() {
        repository.getHeadlinesPagingSource()
    }
}