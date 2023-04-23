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
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
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
        displaySplashScreenIcon()

        val isConnected = isInternetAvailable()
        if (!isConnected) {
            displayErrorMessage()
        } else {
            displaySplashScreenAnimation()
        }
    }

    private fun displaySplashScreenIcon() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        binding.ivNewsFeedIcon.startAnimation(fadeIn)
    }

    private fun displaySplashScreenAnimation() {
        /*TODO setup splash screen animation */
        CoroutineScope(Dispatchers.Main).launch {
            delay(2500)
            // navigateToMainActivity()
            checkBiometricAuth()
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

    private fun checkBiometricAuth() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> initAuthenticationFlow()
            else -> navigateToMainActivity()
        }
    }

    private fun initAuthenticationFlow() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_auth_title))
            .setSubtitle(getString(R.string.biometric_auth_subtitle))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setNegativeButtonText(getString(R.string.biometric_auth_error))
            .build()

        val executor = ContextCompat.getMainExecutor(this)
        val callback = BiometricFinferprintCallback(binding)
        val biometricPrompt = BiometricPrompt(this, executor, callback)
        biometricPrompt.authenticate(promptInfo)

    }

    private fun navigateToMainActivity() {
        /* FETCH SOURCES DATA */
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private inner class BiometricFinferprintCallback(val binding: ActivitySplashScreenBinding)
        : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Snackbar.make(binding.root, R.string.biometric_auth_error, Toast.LENGTH_LONG).show()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Snackbar.make(binding.root, R.string.biometric_auth_error, Toast.LENGTH_LONG).show()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(binding.root.context, "YEAAAAAHHH", Toast.LENGTH_LONG).show()
        }

    }

}