package com.app.nasatask.DI.Network

import com.example.finn.models.NetworkResult
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("users")
    fun getUsersList(
        @Query("page") lastKey: Int,
        @Query("delay") delay: Int
    ): Single<Response<NetworkResult>>

}

