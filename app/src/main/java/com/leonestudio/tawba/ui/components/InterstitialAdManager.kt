package com.leonestudio.tawba.ui.components

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Simple manager for interstitial ads. Call [load] early (e.g. from screen start),
 * and [showIfReady] at a natural breakpoint (e.g. after completing a dhikr cycle
 * or after logging several sadaqa entries).
 *
 * Throttling via [showEveryNActions] avoids annoying users.
 */
class InterstitialAdManager(
    private val adUnitId: String = AdIds.interstitial,
    private val showEveryNActions: Int = 5
) {
    private var ad: InterstitialAd? = null
    private var actionCounter: Int = 0
    private var isLoading: Boolean = false

    fun load(context: Context) {
        if (isLoading || ad != null) return
        isLoading = true
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(loaded: InterstitialAd) {
                    ad = loaded
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    ad = null
                    isLoading = false
                }
            }
        )
    }

    /**
     * Increments the action counter. If threshold is reached and an ad is ready,
     * shows it and reloads a new one.
     */
    fun registerActionAndMaybeShow(activity: Activity) {
        actionCounter++
        if (actionCounter < showEveryNActions) return

        val current = ad
        if (current != null) {
            current.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    ad = null
                    actionCounter = 0
                    load(activity)
                }
            }
            current.show(activity)
        } else {
            load(activity)
        }
    }
}
