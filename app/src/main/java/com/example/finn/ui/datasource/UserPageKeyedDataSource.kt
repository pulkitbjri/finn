package com.example.finn.ui.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.app.nasatask.DI.Network.ApiService
import com.example.finn.models.Data
import com.example.finn.models.NetworkResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import joynt.task.githubassign1.DI.Network.NetworkState
import retrofit2.Response
import java.util.concurrent.TimeUnit

class UserPageKeyedDataSource(
    val service: ApiService,
    val networkErrors: MutableLiveData<NetworkState>?
) : PageKeyedDataSource<Int, Data>() {



    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, Data>) {
        networkErrors?.postValue(NetworkState(NetworkState.INITIAL_LOADING,"INITIAL LOADING..."))

        val currentPage = 1
        service.getUsersList(currentPage,2)
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Response<NetworkResult>>() {
                override fun onSuccess(t: Response<NetworkResult>) {
                    if (t.isSuccessful){
                        networkErrors?.postValue(NetworkState(NetworkState.SUCESS,"Sucess"))

                        if (t.body()?.data!=null)
                            callback.onResult(t.body()?.data!!,0,t.body()?.total!!,null,2)
                    }
                    else
                        networkErrors?.postValue(NetworkState(NetworkState.FAILED,"Failed"))

                }

                override fun onError(e: Throwable) {
                    networkErrors?.postValue(NetworkState(NetworkState.FAILED,"Failed"))

                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, Data>) {
        networkErrors?.postValue(NetworkState(NetworkState.LOADING,"LOADING..."))

        val currentPage = params.key
        service.getUsersList(currentPage,2)
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Response<NetworkResult>>() {
                override fun onSuccess(t: Response<NetworkResult>) {
                    if (t.isSuccessful){
                        networkErrors?.postValue(NetworkState(NetworkState.SUCESS,"Sucess"))
                        if (t.body()?.data!=null){
                            if (t.body()?.total_pages!! > currentPage)
                                callback.onResult(t.body()?.data!!,currentPage+1)
                            else
                                callback.onResult(t.body()?.data!!,null)
                        }
                    }
                    else
                        networkErrors?.postValue(NetworkState(NetworkState.FAILED,"Failed"))

                }

                override fun onError(e: Throwable) {
                    networkErrors?.postValue(NetworkState(NetworkState.FAILED,"Failed"))

                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {

    }
}