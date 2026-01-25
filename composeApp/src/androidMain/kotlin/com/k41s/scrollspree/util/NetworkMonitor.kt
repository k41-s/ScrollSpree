package com.k41s.scrollspree.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NetworkMonitor(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "Connection Lost!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "Back Online", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startMonitoring() {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}