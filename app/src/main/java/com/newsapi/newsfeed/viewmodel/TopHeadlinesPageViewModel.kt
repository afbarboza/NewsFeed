package com.newsapi.newsfeed.viewmodel

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.liveData
import androidx.paging.cachedIn
import com.newsapi.newsfeed.BuildConfig
import com.newsapi.newsfeed.helpers.Helper.Companion.API_PAGE_SIZE
import com.newsapi.newsfeed.helpers.Helper.Companion.API_PREFETCH_DISTANCE
import com.newsapi.newsfeed.model.Article
import com.newsapi.newsfeed.repository.TopHeadlinesPageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopHeadlinesPageViewModel(
    private val repository: TopHeadlinesPageRepository
)
    : ViewModel() {
        val items: LiveData<PagingData<Article>> = Pager(
            config = PagingConfig(pageSize = API_PAGE_SIZE,
                prefetchDistance = API_PREFETCH_DISTANCE,
                enablePlaceholders = false),
                pagingSourceFactory = { repository.getHeadlinesPagingSource() }
        )
            .liveData
            .cachedIn(viewModelScope)

        private val _newsProviderName: MutableLiveData<String> = MutableLiveData()
        val newsProviderName:  LiveData<String> = _newsProviderName
}