package com.example.finn.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.nasatask.DI.Network.ApiService
import com.example.finn.models.Data
import com.example.finn.ui.datasource.UserDataFactory
import joynt.task.githubassign1.DI.Network.NetworkState
import java.util.concurrent.Executors
import javax.inject.Inject

class MainViewModel @Inject constructor(var service : ApiService) :  ViewModel() {

    var networkErrors: MutableLiveData<NetworkState>? = MutableLiveData()
    var dataSource: DataSource<Int, Data>? = null
    var repoPagedList: LiveData<PagedList<Data>>? = null
    init {
        makeAPICall()
    }
    private fun makeAPICall() {
        val dataSourceFactory = UserDataFactory(networkErrors,service)
        dataSource = dataSourceFactory.create()
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(6)
            .setPageSize(6)
            .setEnablePlaceholders(true)
            .build()

        repoPagedList = LivePagedListBuilder(dataSourceFactory, config)
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .setInitialLoadKey(0)
            .build()

    }
}
