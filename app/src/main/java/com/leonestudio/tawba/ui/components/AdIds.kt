package com.leonestudio.tawba.ui.components

/**
 * AdMob unit IDs. Using Google's official test IDs.
 * IMPORTANT: Replace with your real unit IDs from the AdMob console before release.
 * Test IDs reference: https://developers.google.com/admob/android/test-ads
 */
object AdIds {
    const val BANNER_TEST = "ca-app-pub-3940256099942544/6300978111"
    const val INTERSTITIAL_TEST = "ca-app-pub-3940256099942544/1033173712"

    // Replace these before going to production
    const val BANNER_PROD = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
    const val INTERSTITIAL_PROD = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"

    // Toggle when ready for release
    const val USE_TEST_ADS = true

    val banner: String get() = if (USE_TEST_ADS) BANNER_TEST else BANNER_PROD
    val interstitial: String get() = if (USE_TEST_ADS) INTERSTITIAL_TEST else INTERSTITIAL_PROD
}
