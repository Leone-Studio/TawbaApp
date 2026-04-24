package com.leonestudio.tawba.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonestudio.tawba.R
import com.leonestudio.tawba.TawbaApplication
import com.leonestudio.tawba.ui.components.BannerAd
import com.leonestudio.tawba.viewmodel.AdhkarViewModel
import com.leonestudio.tawba.viewmodel.SadaqaViewModel
import com.leonestudio.tawba.viewmodel.SettingsViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as TawbaApplication

    val adhkarVm: AdhkarViewModel = viewModel(
        factory = AdhkarViewModel.Factory(app.database.dhikrDao())
    )
    val sadaqaVm: SadaqaViewModel = viewModel(
        factory = SadaqaViewModel.Factory(app.database.sadaqaDao())
    )
    val settingsVm: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(app.userPreferences)
    )

    val adhkar by adhkarVm.adhkar.collectAsStateWithLifecycle()
    val totalDhikrCount by adhkarVm.totalCount.collectAsStateWithLifecycle()
    val totalSadaqa by sadaqaVm.totalAmount.collectAsStateWithLifecycle()
    val monthSadaqa by sadaqaVm.thisMonthAmount.collectAsStateWithLifecycle()
    val sadaqaCount by sadaqaVm.entryCount.collectAsStateWithLifecycle()
    val currency by settingsVm.currency.collectAsStateWithLifecycle()

    val totalTarget = adhkar.sumOf { it.targetCount }
    val overallProgress = if (totalTarget > 0) {
        totalDhikrCount.toFloat() / totalTarget
    } else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_stats)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Adhkar section
                SectionHeader(
                    title = stringResource(R.string.adhkar_stats),
                    icon = Icons.AutoMirrored.Filled.MenuBook
                )
                StatCard(
                    label = stringResource(R.string.total_dhikr_count),
                    value = totalDhikrCount.toString(),
                    secondary = stringResource(R.string.of_target, totalTarget)
                )
                ProgressCard(
                    label = stringResource(R.string.overall_progress),
                    progress = overallProgress.coerceIn(0f, 1f),
                    percentText = "${(overallProgress * 100).toInt()}%"
                )
                PerCategoryBreakdown(adhkar = adhkar)

                Spacer(modifier = Modifier.height(8.dp))

                // Sadaqa section
                SectionHeader(
                    title = stringResource(R.string.sadaqa_stats),
                    icon = Icons.Filled.Favorite
                )
                StatCard(
                    label = stringResource(R.string.total_sadaqa),
                    value = String.format(Locale.getDefault(), "%.2f %s", totalSadaqa, currency),
                    secondary = stringResource(R.string.entries_count, sadaqaCount)
                )
                StatCard(
                    label = stringResource(R.string.this_month),
                    value = String.format(Locale.getDefault(), "%.2f %s", monthSadaqa, currency),
                    secondary = null
                )
            }
            BannerAd(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(0.dp))
        Text(
            text = "  $title",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, secondary: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            if (secondary != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = secondary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProgressCard(label: String, progress: Float, percentText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = percentText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
private fun PerCategoryBreakdown(adhkar: List<com.leonestudio.tawba.data.Dhikr>) {
    if (adhkar.isEmpty()) return
    val grouped = adhkar.groupBy { it.category }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.by_category),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            grouped.forEach { (category, items) ->
                val catCurrent = items.sumOf { it.currentCount }
                val catTarget = items.sumOf { it.targetCount }
                val catProgress = if (catTarget > 0) catCurrent.toFloat() / catTarget else 0f
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(0.35f)
                    )
                    LinearProgressIndicator(
                        progress = { catProgress.coerceIn(0f, 1f) },
                        modifier = Modifier.weight(0.5f).height(8.dp)
                    )
                    Text(
                        text = "$catCurrent / $catTarget",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(0.35f).padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
