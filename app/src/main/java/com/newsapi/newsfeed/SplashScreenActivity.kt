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
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.newsapi.newsfeed.databinding.ActivitySplashScreenBinding
import com.newsapi.newsfeed.helpers.Helper.Companion.NEWS_PROVIDER_NAME_PARAM
import com.newsapi.newsfeed.view.MainActivity
import com.newsapi.newsfeed.viewmodel.SourcesViewModel
import com.newsapi.newsfeed.viewmodel.SourcesViewModelFactory

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sourcesViewModel: SourcesViewModel

    private var newsProvider: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        initDataListeners()
        initViews()
    }

    private fun initViewModel() {
       val viewModelFactory = SourcesViewModelFactory( Injection.provideTopHeadlinesPageRepository() )
        sourcesViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SourcesViewModel::class.java)

    }

    private fun initDataListeners() {
        sourcesViewModel.errorStatus.observe(this) { error ->
            if (error) {
                displayErrorMessage()
            }
        }

        sourcesViewModel.newsProviderName.observe(this) { newsProviderName ->
            this.newsProvider = newsProviderName
        }

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
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)

        fadeIn.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                initData()
            }

            override fun onAnimationEnd(p0: Animation?) {
                checkBiometricAuth()
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        binding.ivNewsFeedIcon.startAnimation(fadeIn)
    }

    private fun initData() {
        sourcesViewModel.getHeadlinesPagingSource()
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
        val callback = BiometricFingerprintCallback(binding)
        val biometricPrompt = BiometricPrompt(this, executor, callback)
        biometricPrompt.authenticate(promptInfo)

    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        if (newsProvider != "") {
            intent.putExtra(NEWS_PROVIDER_NAME_PARAM, newsProvider)
            startActivity(intent)
        } else {
            displayErrorMessage()
        }
    }

    private inner class BiometricFingerprintCallback(val binding: ActivitySplashScreenBinding)
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
            navigateToMainActivity()
        }

    }

}