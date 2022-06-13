package com.eone.submission1_bpai.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eone.submission1_bpai.data.model.common.Story


@Database(
    entities = [Story::class],
    version = 1
)

abstract class StoryDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: StoryDatabase? = null

        fun getDatabase(context: Context): StoryDatabase? {
            if (INSTANCE == null) {
                synchronized(StoryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        StoryDatabase::class.java,
                        "story_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun storyDao(): StoryDao
}