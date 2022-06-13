package com.eone.submission1_bpai.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eone.submission1_bpai.data.model.common.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(story : Story)

    @Query("DELETE FROM story")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM story")
    fun findAll() : Cursor

}