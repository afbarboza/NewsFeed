package com.newsapi.newsfeed

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
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
            navigateToHeadlineNews()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return if (capability != null) {
            val netCapabilityInternet = capability.hasCapability(NET_CAPABILITY_INTERNET)
            val netCapabilityValidated = capability.hasCapability(NET_CAPABILITY_VALIDATED)
            return netCapabilityInternet && netCapabilityValidated
        } else {
            false
        }
    }

    private fun displayErrorMessage() {
        binding.tvErrorInternet.visibility = View.VISIBLE
    }

    private fun navigateToHeadlineNews() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}