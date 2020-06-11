package com.example.finn.ui.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.nasatask.DI.Network.ApiService
import com.example.finn.models.Data
import joynt.task.githubassign1.DI.Network.NetworkState

class UserDataFactory(
    var networkErrors: MutableLiveData<NetworkState>?,
    val service: ApiService
) : DataSource.Factory<Int, Data>() {

    val mutableLiveData: MutableLiveData<UserPageKeyedDataSource> = MutableLiveData()

    private var feedDataSource: UserPageKeyedDataSource? = null


    override fun create(): DataSource<Int, Data>? {
        feedDataSource = UserPageKeyedDataSource(service,networkErrors)
        mutableLiveData.postValue(feedDataSource)
        return feedDataSource
    }
    fun getItemLiveDataSource(): UserPageKeyedDataSource? {
        return feedDataSource
    }

}