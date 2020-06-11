package com.example.finn.ui.main

import android.os.Bundle
import android.view.View.GONE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nasa.Base.BaseActivity
import com.example.finn.R
import com.example.finn.ui.RecyclerPagedAdapter
import com.google.android.material.snackbar.Snackbar
import joynt.task.githubassign1.DI.Network.NetworkState.Companion.SUCESS
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>() {

    @Inject
    internal lateinit var viewModel: MainViewModel
    @Inject
    internal lateinit var adapter: RecyclerPagedAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setObservers()
        setRecycler()
        shimmerframeLayout.startShimmerAnimation()
    }

    private fun setRecycler() {
        recyclerr.layoutManager=LinearLayoutManager(this)
        recyclerr.adapter=adapter
    }

    private fun setObservers() {
        viewModel.repoPagedList?.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.networkErrors?.observe(this, Observer {
            if (it.status==SUCESS){
                shimmerframeLayout.stopShimmerAnimation()
                shimmerframeLayout.visibility=GONE
            }
        })
    }

    override fun layoutRes(): Int {
        return R.layout.main_activity
    }

    override fun getViewModel(): MainViewModel {
        return viewModel
    }

    override fun isConnectedToInternet(status: Boolean) {
        if (status && adapter.currentList.isNullOrEmpty()) {
            adapter.currentList?.dataSource?.invalidate()
        }
        if (status)
        {
            Snackbar.make(container, "Internet Connected", Snackbar.LENGTH_LONG).show()
        }
        else
        {
            Snackbar.make(container, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    }
    override fun onResume() {
        super.onResume()
        shimmerframeLayout.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerframeLayout.stopShimmerAnimation()
        super.onPause()
    }
}