package com.leonestudio.tawba.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Dhikr::class, SadaqaEntry::class, JuzEntry::class],
    version = 2,
    exportSchema = false
)
abstract class TawbaDatabase : RoomDatabase() {

    abstract fun dhikrDao(): DhikrDao
    abstract fun sadaqaDao(): SadaqaDao
    abstract fun juzDao(): JuzDao

    companion object {
        @Volatile private var INSTANCE: TawbaDatabase? = null

        fun getInstance(context: Context): TawbaDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    TawbaDatabase::class.java,
                    "tawba.db"
                )

                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.dhikrDao()?.insertAll(DhikrDefaults.list)
                                INSTANCE?.juzDao()?.insertAll(JuzDefaults.list)
                            }
                        }
                    })
                    .build()
                INSTANCE = db
                db
            }
        }
    }
}