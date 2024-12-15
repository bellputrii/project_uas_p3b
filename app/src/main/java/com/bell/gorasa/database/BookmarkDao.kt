package com.bell.gorasa.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Insert
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmarked_items")
    suspend fun getAllBookmarks(): List<Bookmark>
}
