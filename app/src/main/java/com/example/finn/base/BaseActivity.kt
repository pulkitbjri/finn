package com.app.nasa.Base

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import com.example.finn.networkstate.NetworkChangeReceiver
import com.example.finn.networkstate.ObservableObject
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.annotations.Nullable
import java.util.*


abstract class BaseActivity<T : ViewModel>  : DaggerAppCompatActivity(),Observer {
    override fun update(o: Observable?, arg: Any) {
        isInternetAvailable = arg as Boolean
        isConnectedToInternet(isInternetAvailable)

    }

    private lateinit var networkReciever: BroadcastReceiver
    private var isInternetAvailable: Boolean = false
    private var viewModel: T? = null

    abstract fun isConnectedToInternet(status : Boolean)
    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
        this.viewModel = viewModel ?: getViewModel()

    }

    abstract fun getViewModel(): T

    override fun onResume() {
        super.onResume()
        networkReciever=NetworkChangeReceiver()
        registerNetworkBroadcast()
        ObservableObject.instance.addObserver(this)
    }

    override fun onPause() {
        super.onPause()
        unregisterNetworkChanges()
        ObservableObject.instance.deleteObserver(this)
    }

    private fun registerNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkReciever, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkReciever, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    protected open fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkReciever)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}