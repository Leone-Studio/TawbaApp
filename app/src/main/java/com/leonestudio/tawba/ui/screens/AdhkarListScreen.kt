package com.leonestudio.tawba.ui.screens

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonestudio.tawba.R
import com.leonestudio.tawba.TawbaApplication
import com.leonestudio.tawba.data.Dhikr
import com.leonestudio.tawba.ui.components.BannerAd
import com.leonestudio.tawba.ui.components.InterstitialAdManager
import com.leonestudio.tawba.ui.theme.ArabicDhikrStyle
import com.leonestudio.tawba.ui.theme.BgBlush
import com.leonestudio.tawba.ui.theme.BgSurface
import com.leonestudio.tawba.ui.theme.Gold500
import com.leonestudio.tawba.ui.theme.Mauve500
import com.leonestudio.tawba.ui.theme.Mauve700
import com.leonestudio.tawba.ui.theme.MauveSoft
import com.leonestudio.tawba.ui.theme.TawbaTheme
import com.leonestudio.tawba.ui.theme.TextPrimary
import com.leonestudio.tawba.ui.theme.TextSecondary
import com.leonestudio.tawba.viewmodel.AdhkarViewModel
import com.leonestudio.tawba.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdhkarListScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as TawbaApplication
    val vm: AdhkarViewModel = viewModel(
        factory = AdhkarViewModel.Factory(app.database.dhikrDao())
    )
    val settingsVm: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(app.userPreferences)
    )

    val adhkar by vm.adhkar.collectAsStateWithLifecycle()
    val language by settingsVm.language.collectAsStateWithLifecycle()
    val vibrationEnabled by settingsVm.vibration.collectAsStateWithLifecycle()

    val interstitialManager = remember { InterstitialAdManager() }
    DisposableEffect(Unit) {
        interstitialManager.load(context)
        onDispose { }
    }

    Scaffold(
        containerColor = BgBlush,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.nav_adhkar),
                        color = Mauve700,
                        fontWeight = FontWeight.Medium
                    )
                },
                actions = {
                    IconButton(onClick = { vm.resetAll() }) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = stringResource(R.string.reset_all),
                            tint = Mauve500
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgBlush,
                    titleContentColor = Mauve700,
                    actionIconContentColor = Mauve500
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(BgBlush)) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(adhkar, key = { it.id }) { dhikr ->
                    DhikrCard(
                        dhikr = dhikr,
                        language = language,
                        onTap = {
                            vm.incrementCount(dhikr)
                            if (vibrationEnabled) vibrate(context)
                            val willComplete = dhikr.currentCount + 1 >= dhikr.targetCount
                            if (willComplete && context is Activity) {
                                interstitialManager.registerActionAndMaybeShow(context)
                            }
                        },
                        onReset = { vm.resetCount(dhikr) }
                    )
                }
            }
            BannerAd(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun DhikrCard(
    dhikr: Dhikr,
    language: String,
    onTap: () -> Unit,
    onReset: () -> Unit
) {
    val progress = if (dhikr.targetCount > 0) {
        dhikr.currentCount.toFloat() / dhikr.targetCount
    } else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")
    val isDone = dhikr.currentCount >= dhikr.targetCount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDone) { onTap() },
        colors = CardDefaults.cardColors(containerColor = BgSurface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (isDone) {
            androidx.compose.foundation.BorderStroke(1.5.dp, Gold500)
        } else null
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp)) {
            Text(
                text = dhikr.arabic,
                style = ArabicDhikrStyle.copy(
                    color = if (isDone) Gold500 else Mauve700
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            val translation = when (language) {
                "fr" -> dhikr.translationFr
                "en" -> dhikr.translationEn
                else -> dhikr.transliteration
            }
            Text(
                text = translation,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onReset,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "${dhikr.currentCount}",
                    color = Mauve500,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp)
                )
                Text(
                    text = " / ${dhikr.targetCount}",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.size(14.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MauveSoft)
                ) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = Mauve500,
                        trackColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                }
            }
        }
    }
}

private fun vibrate(context: Context) {
    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    } catch (_: Exception) {
    }
}

@Preview(showBackground = true)
@Composable
private fun DhikrCardPreview() {
    TawbaTheme {
        DhikrCard(
            dhikr = com.leonestudio.tawba.data.DhikrDefaults.list.first().copy(currentCount = 33),
            language = "ar",
            onTap = {},
            onReset = {}
        )
    }
}