package com.leonestudio.tawba.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single dhikr with tri-lingual text and a target repetition count.
 * Stored in DB so user-tracked counts persist.
 */
@Entity(tableName = "adhkar")
data class Dhikr(
    @PrimaryKey val id: Int,
    val arabic: String,
    val transliteration: String,
    val translationFr: String,
    val translationEn: String,
    val targetCount: Int,
    val currentCount: Int = 0,
    val category: String // "morning", "evening", "tawba", "general"
)

/**
 * Preset list of adhkar bundled with the app.
 * These are used as defaults on first launch.
 */
object DhikrDefaults {
    val list: List<Dhikr> = listOf(
        Dhikr(
            id = 1,
            arabic = "أَسْتَغْفِرُ اللَّهَ",
            transliteration = "Astaghfirullah",
            translationFr = "Je demande pardon à Allah",
            translationEn = "I seek forgiveness from Allah",
            targetCount = 100,
            category = "tawba"
        ),
        Dhikr(
            id = 2,
            arabic = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullah al-Azim wa atubu ilayh",
            translationFr = "Je demande pardon à Allah le Très Grand et je me repens à Lui",
            translationEn = "I seek forgiveness from Allah the Almighty and I repent to Him",
            targetCount = 100,
            category = "tawba"
        ),
        Dhikr(
            id = 3,
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            transliteration = "SubhanAllahi wa bihamdih",
            translationFr = "Gloire à Allah et à Lui la louange",
            translationEn = "Glory be to Allah and praise be to Him",
            targetCount = 100,
            category = "general"
        ),
        Dhikr(
            id = 4,
            arabic = "لَا إِلَهَ إِلَّا اللَّهُ",
            transliteration = "La ilaha illa Allah",
            translationFr = "Il n'y a de divinité qu'Allah",
            translationEn = "There is no god but Allah",
            targetCount = 100,
            category = "general"
        ),
        Dhikr(
            id = 5,
            arabic = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ",
            transliteration = "Allahumma salli 'ala Muhammad",
            translationFr = "Ô Allah, prie sur Muhammad",
            translationEn = "O Allah, send blessings upon Muhammad",
            targetCount = 100,
            category = "general"
        ),
        Dhikr(
            id = 6,
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            transliteration = "La hawla wa la quwwata illa billah",
            translationFr = "Il n'y a de force ni de puissance qu'en Allah",
            translationEn = "There is no power nor strength except with Allah",
            targetCount = 100,
            category = "general"
        ),
        Dhikr(
            id = 7,
            arabic = "سُبْحَانَ اللَّهِ",
            transliteration = "SubhanAllah",
            translationFr = "Gloire à Allah",
            translationEn = "Glory be to Allah",
            targetCount = 33,
            category = "morning"
        ),
        Dhikr(
            id = 8,
            arabic = "الْحَمْدُ لِلَّهِ",
            transliteration = "Alhamdulillah",
            translationFr = "Louange à Allah",
            translationEn = "All praise is due to Allah",
            targetCount = 33,
            category = "morning"
        ),
        Dhikr(
            id = 9,
            arabic = "اللَّهُ أَكْبَرُ",
            transliteration = "Allahu Akbar",
            translationFr = "Allah est le plus grand",
            translationEn = "Allah is the Greatest",
            targetCount = 34,
            category = "morning"
        ),
        Dhikr(
            id = 10,
            arabic = "حَسْبِيَ اللَّهُ لَا إِلَهَ إِلَّا هُوَ",
            transliteration = "Hasbi Allahu la ilaha illa Huwa",
            translationFr = "Allah me suffit, il n'y a de divinité que Lui",
            translationEn = "Allah is sufficient for me, there is no god but Him",
            targetCount = 7,
            category = "evening"
        )
    )
}
