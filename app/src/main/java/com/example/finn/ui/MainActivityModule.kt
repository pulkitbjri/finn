package com.example.finn.ui.main

import com.example.finn.ui.RecyclerPagedAdapter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    internal fun provideRepoRecyclerAdapter(): RecyclerPagedAdapter {
        return RecyclerPagedAdapter()
    }
}