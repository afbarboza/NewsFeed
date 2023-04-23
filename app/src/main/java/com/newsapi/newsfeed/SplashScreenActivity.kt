package com.newsapi.newsfeed

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.newsapi.newsfeed.databinding.ActivitySplashScreenBinding
import com.newsapi.newsfeed.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        val isConnected = isInternetAvailable()
        if (!isConnected) {
            displayErrorMessage()
        } else {
            displaySplashScreenAnimation()
        }
    }

    private fun displaySplashScreenAnimation() {
        /*TODO setup splash screen animation */
        CoroutineScope(Dispatchers.Main).launch {
            delay(2500)
            navigateToMainActivity()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val capability = getInternetCapability()

        return if (capability != null) {
            val netCapabilityInternet = capability.hasCapability(NET_CAPABILITY_INTERNET)
            val netCapabilityValidated = capability.hasCapability(NET_CAPABILITY_VALIDATED)
            netCapabilityInternet && netCapabilityValidated
        } else {
            false
        }
    }

    private fun getInternetCapability():  NetworkCapabilities? {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    }

    private fun displayErrorMessage() {
        binding.tvErrorInternet.visibility = View.VISIBLE
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}