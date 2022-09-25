package com.eone.submission2_bpai.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eone.submission2_bpai.data.model.common.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(story : Story)

    @Query("DELETE FROM story")
    suspend fun deleteAll()

    @Query("SELECT * FROM story")
    fun getAll() : PagingSource<Int, Story>

}