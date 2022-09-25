package com.eone.submission2_bpai.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eone.submission2_bpai.data.model.common.Story
import com.eone.submission2_bpai.local.RemoteKeysDao
import com.eone.submission2_bpai.local.StoryDao
import com.eone.submission2_bpai.local.entity.RemoteKeys


@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1, exportSchema = false
)

abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}