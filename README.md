# Tawba — Islamic Adhkar & Sadaqa Tracker

An Android app for tracking daily **adhkar** (remembrances) and **sadaqa** (charitable giving), built with **Kotlin + Jetpack Compose**.

Built by **Leone Studio** · Min SDK 29 (Android 10) · Target SDK 34

---

## ✨ Features

- 📿 **Adhkar counter** — tap-to-count with haptic feedback and animated progress per dhikr
- 💰 **Sadaqa log** — categorized entries (general, zakat, fitr, masjid, orphan, other) in any currency
- 📊 **Stats dashboard** — per-category progress breakdown + monthly/total sadaqa totals
- 🌍 **Trilingual** — Arabic (default), French, English, with RTL support for Arabic
- 🌙 **Dark mode** toggle
- 📳 **Vibration** toggle
- 💵 **Custom currency** (default MAD)
- 📱 **AdMob** Banner + Interstitial integration (currently using Google test IDs)
- 💾 **Offline-first** — all data in a local Room database

## 🏗️ Architecture

```
com.leonestudio.tawba/
├── data/              # Room entities, DAOs, DB, UserPreferences (DataStore)
├── ui/
│   ├── components/    # BannerAd, InterstitialAdManager, AdIds
│   ├── navigation/    # TawbaNavHost with bottom bar
│   ├── screens/       # AdhkarListScreen, SadaqaScreen, StatsScreen, SettingsScreen
│   └── theme/         # Color, Type, Theme (Islamic green + gold palette)
├── util/              # LocaleHelper, DateUtils
├── viewmodel/         # AdhkarViewModel, SadaqaViewModel, SettingsViewModel
├── TawbaApplication   # Initializes MobileAds + exposes DB & prefs
└── MainActivity       # Hosts Compose UI, applies locale
```

**Stack:** Kotlin 1.9.24 · Jetpack Compose (BOM 2024.08.00) · Material 3 · Navigation Compose · Room 2.6.1 · DataStore · Play Services Ads 23.2.0

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17** (bundled with recent Android Studio versions)
- An Android device/emulator running **Android 10 (API 29)** or higher

### Setup

```bash
# 1. Unzip and open
unzip TawbaApp.zip
cd TawbaApp

# 2. Open in Android Studio
# File → Open → select the TawbaApp folder
# Wait for Gradle sync to complete (first sync will download ~200MB of deps)

# 3. Run
# Either click ▶ Run in Android Studio, or from terminal:
./gradlew installDebug
```

If Gradle sync fails on first open, try **File → Invalidate Caches / Restart**.

### Build a release APK

```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

You'll need to [sign it](https://developer.android.com/studio/publish/app-signing) before uploading to Google Play.

## 📱 AdMob Configuration

The app currently ships with **Google's official test ad unit IDs** so you can test without creating an AdMob account. They're safe but only show test ads.

**Before publishing to Play Store**, replace them:

### 1. Update the AdMob App ID in `AndroidManifest.xml`

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-YOUR_REAL_APP_ID~XXXXXXXXX" />
```

### 2. Update the ad unit IDs in `ui/components/AdIds.kt`

```kotlin
const val BANNER_PROD = "ca-app-pub-YOUR_PUBLISHER_ID/BANNER_UNIT_ID"
const val INTERSTITIAL_PROD = "ca-app-pub-YOUR_PUBLISHER_ID/INTERSTITIAL_UNIT_ID"

// Flip this to false
const val USE_TEST_ADS = false
```

Interstitials show every **5th** dhikr-completion. Tune via `InterstitialAdManager(showEveryNActions = N)` in `AdhkarListScreen.kt`.

## 🌍 Translation & RTL

String resources live in:

- `app/src/main/res/values/strings.xml` — English (default)
- `app/src/main/res/values-ar/strings.xml` — Arabic (default locale on first launch)
- `app/src/main/res/values-fr/strings.xml` — French

Language can be switched at runtime via **Settings → Language**. The activity recreates to apply the new locale + RTL layout direction.

Arabic dhikr text itself is bundled in `data/Dhikr.kt` (`DhikrDefaults.list`) — it's data, not a string resource, because each dhikr carries its own three translations.

## ➕ Adding a New Dhikr

Edit `app/src/main/java/com/leonestudio/tawba/data/Dhikr.kt` and add to `DhikrDefaults.list`:

```kotlin
Dhikr(
    id = 11,
    arabic = "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
    transliteration = "Allahumma innaka 'afuwwun tuhibbu al-'afwa fa'fu 'anni",
    translationFr = "Ô Allah, Tu es Pardonneur et Tu aimes le pardon, alors pardonne-moi",
    translationEn = "O Allah, You are Pardoning and love pardon, so pardon me",
    targetCount = 10,
    category = "tawba"
)
```

> ⚠️ Existing users will **not** see new defaults unless you bump the Room DB version and write a migration, or add one-time insertion logic in `TawbaDatabase`. On a fresh install it just works.

Valid categories: `"tawba"`, `"general"`, `"morning"`, `"evening"`. Add new ones freely — the stats screen groups by whatever string you use.

## 🎨 Theming

Color palette lives in `ui/theme/Color.kt`:

- **Primary:** Islamic green (`#2E7D32` light / `#66BB6A` dark)
- **Secondary:** Gold accent (`#D4AF37`)
- **Background:** Warm sand (`#FAF7F2` light / `#1C1B1A` dark)

Launcher icon is an adaptive icon (green background + gold crescent & star). Foreground vector at `res/drawable/ic_launcher_foreground.xml`; backgrounds per-density in `res/mipmap-*`.

## 📊 Data Model

Two Room tables:

- **`adhkar`** — one row per dhikr with `currentCount` persisted (survives app restarts)
- **`sadaqa`** — one row per sadaqa entry (amount, currency, category, note, timestamp)

User settings live in **DataStore Preferences** (`tawba_prefs`):
- `language` (ar/fr/en)
- `dark_mode` (bool)
- `vibration` (bool)
- `currency` (default `MAD`)

## ✅ Release Checklist

- [ ] Replace all AdMob test IDs with production IDs (`AdIds.kt` + `AndroidManifest.xml`)
- [ ] Set `USE_TEST_ADS = false`
- [ ] Bump `versionCode` + `versionName` in `app/build.gradle.kts`
- [ ] Add your signing config (`release` keystore)
- [ ] Enable R8 shrinking (already on in release build type)
- [ ] Test on a physical device with Arabic RTL + dark mode
- [ ] Register a Play Console listing under your Leone Studio developer account
- [ ] Generate screenshots in all 3 languages
- [ ] Add a privacy policy URL (required for AdMob apps)
- [ ] Complete the Data Safety form in Play Console

## 📄 License

© Leone Studio. All rights reserved.

---

*Built with Claude — `claude.ai`*
