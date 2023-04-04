package com.example.randomquote_retrofit_example.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Debug
import android.util.Log.i


class NetworkUtils {
    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return this.getNetworkCapabilities(this.activeNetwork)?.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_INTERNET
                    ) ?: false
                } else {
                    (@Suppress("DEPRECATION")
                    return this.activeNetworkInfo?.isConnected ?: false)
                }
            }
        }
    }
}