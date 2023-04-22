package com.newsapi.newsfeed

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.newsapi.newsfeed.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isConnected = isInternetAvailable()
        if (!isConnected) {
            displayErrorMessage()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capability == null) {
            return false
        }

        val netCapabilityInternet = capability.hasCapability(NET_CAPABILITY_INTERNET)
        val netCapabilityValidated = capability.hasCapability(NET_CAPABILITY_VALIDATED)
        return netCapabilityInternet && netCapabilityValidated
    }

    private fun displayErrorMessage() {
        binding.tvErrorInternet.visibility = View.VISIBLE
    }

}