package com.leonestudio.tawba.ui.theme

enum class ThemeChoice(val id: String) {
    DIVINE_GRACE("divine_grace"),
    MIDNIGHT_NOOR("midnight_noor"),
    ROSE_GARDEN("rose_garden"),
    GREEN_SERENITY("green_serenity");

    companion object {
        fun fromId(id: String?): ThemeChoice =
            entries.firstOrNull { it.id == id } ?: DIVINE_GRACE
    }
}