package com.magnus.deal.calc


import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.lang.ref.WeakReference

private const val TAG = "MainAdHandler"
private const val REPEAT_FACTOR = 1.2

class MainAdHandler : DefaultLifecycleObserver {

    private var interstitialAd: InterstitialAd? = null
    private val handler = Handler(Looper.getMainLooper())
    private var activityRef: WeakReference<Activity>? = null
    private var canShowAd = false
    private var adsShown = 0

    fun bind(activity: AppCompatActivity) {
        activity.lifecycle.addObserver(this)
        activityRef = WeakReference(activity)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        scheduleRepeatingAdsDelayed()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        activityRef?.clear()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        canShowAd = false
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        canShowAd = true
    }

    private fun scheduleRepeatingAdsDelayed() {
        Log.d(TAG, "Ad scheduled")
        var delay = 25 * 1000L

        if (adsShown > 0) {
            delay = (delay * (REPEAT_FACTOR * adsShown)).toLong()
        }

        loadAd()
        handler.postDelayed({
            showAd {
                scheduleRepeatingAdsDelayed()
            }
        }, delay)
    }

    private fun loadAd() {
        Log.d(TAG, "Ad loading")
        val context = activityRef?.get() ?: return
        val adRequest = AdRequest.Builder().build()
        val adUnitID = context.getString(R.string.admob_interstitial_id)
        InterstitialAd.load(context, adUnitID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ads: InterstitialAd) {
                Log.d(TAG, "Ad loaded")
                interstitialAd = ads
            }

            override fun onAdFailedToLoad(err: LoadAdError) {
                Log.d(TAG, "Ad failed to load ${err.message} ${err.code}")
            }
        })
    }

    private fun showAd(onDismiss: () -> Unit = {}) {
        Log.d(TAG, "Ad shown")
        if (!canShowAd) return
        val activity = activityRef?.get() ?: return
        val ad = interstitialAd ?: return
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed")
                interstitialAd = null
                onDismiss()
            }
        }
        ad.show(activity)
        adsShown++
    }
}