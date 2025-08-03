package com.magnus.deal.calc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    private lateinit var mAdView: AdView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)
        MainAdHandler().bind(this)

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val rootView = findViewById<ViewGroup>(R.id.root_view)
        WindowCompat.getInsetsController(window, rootView).isAppearanceLightStatusBars = true

        val webView = findViewById<WebView>(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("file:///android_asset/index.html")
    }

    override fun onPause() {
        super.onPause()
        mAdView.pause()
    }

    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdView.destroy()
    }
}