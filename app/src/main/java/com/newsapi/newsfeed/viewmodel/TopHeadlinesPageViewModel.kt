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
            config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 1, enablePlaceholders = false),
            pagingSourceFactory = { repository.getHeadlinesPagingSource() }
        )
            .liveData
            .cachedIn(viewModelScope)

        private val _newsProviderName: MutableLiveData<String> = MutableLiveData()
        val newsProviderName:  LiveData<String> = _newsProviderName

    fun getHeadlinesPagingSource() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getAllSources()
            if (response.body() != null) {
                val allSources = response.body()!!.sources
                if (!allSources.isNullOrEmpty()) {
                    val currentSource = allSources.first {
                        it.id == BuildConfig.source_id
                    }

                    withContext(Dispatchers.Main) {
                        _newsProviderName.value = currentSource.name
                    }
                }
            }
        }
    }
}