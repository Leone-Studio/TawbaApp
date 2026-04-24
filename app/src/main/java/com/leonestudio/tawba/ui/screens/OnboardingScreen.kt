package com.leonestudio.tawba.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leonestudio.tawba.TawbaApplication
import com.leonestudio.tawba.ui.theme.*
import com.leonestudio.tawba.viewmodel.SettingsViewModel

private data class ThemePreview(
    val choice: ThemeChoice,
    val label: String,
    val arabicLabel: String,
    val bg: Color,
    val surface: Color,
    val primary: Color,
    val accent: Color,
    val text: Color
)

private val themePreviews = listOf(
    ThemePreview(
        ThemeChoice.DIVINE_GRACE,
        "Divine Grace",
        "نعمة إلهية",
        DG_Bg, DG_Surface, DG_Primary, DG_Accent, DG_TextPrimary
    ),
    ThemePreview(
        ThemeChoice.MIDNIGHT_NOOR,
        "Midnight Noor",
        "نور الليل",
        MN_Bg, MN_Surface, MN_Primary, MN_Accent, MN_TextPrimary
    ),
    ThemePreview(
        ThemeChoice.ROSE_GARDEN,
        "Rose Garden",
        "حديقة الورد",
        RG_Bg, RG_Surface, RG_Primary, RG_Accent, RG_TextPrimary
    ),
    ThemePreview(
        ThemeChoice.GREEN_SERENITY,
        "Green Serenity",
        "هدوء الطبيعة",
        GS_Bg, GS_Surface, GS_Primary, GS_Accent, GS_TextPrimary
    )
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as TawbaApplication
    val vm: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory(app.userPreferences)
    )

    var selected by remember { mutableStateOf(ThemeChoice.DIVINE_GRACE) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "تَوْبَة",
                style = ArabicDhikrStyle.copy(
                    fontSize = 44.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "اختر الأسلوب الذي يُناسبك",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Choose your vibe",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ThemeCard(
                        preview = themePreviews[0],
                        selected = selected == themePreviews[0].choice,
                        onClick = { selected = themePreviews[0].choice },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeCard(
                        preview = themePreviews[1],
                        selected = selected == themePreviews[1].choice,
                        onClick = { selected = themePreviews[1].choice },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ThemeCard(
                        preview = themePreviews[2],
                        selected = selected == themePreviews[2].choice,
                        onClick = { selected = themePreviews[2].choice },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeCard(
                        preview = themePreviews[3],
                        selected = selected == themePreviews[3].choice,
                        onClick = { selected = themePreviews[3].choice },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    vm.setTheme(selected)
                    vm.setOnboarded(true)
                    onFinished()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "ابدأ • Continue",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ThemeCard(
    preview: ThemePreview,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) preview.primary else Color.Transparent
    val borderWidth = if (selected) 3.dp else 0.dp

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(preview.bg)
            .border(borderWidth, borderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(preview.surface),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ذِكْر",
                    style = ArabicDhikrStyle.copy(
                        fontSize = 22.sp,
                        color = preview.text
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(width = 50.dp, height = 6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(preview.primary.copy(alpha = 0.25f))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 35.dp, height = 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(preview.primary)
                        )
                    }
                    Spacer(modifier = Modifier.size(6.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(preview.accent)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = preview.arabicLabel,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = preview.text,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = preview.label,
            style = MaterialTheme.typography.labelSmall,
            color = preview.text.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}