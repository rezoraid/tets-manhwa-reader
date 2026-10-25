package com.tets.manhwa.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "bookmarks",
    indices = [Index(value = ["id"], unique = true)]
)
data class BookmarkEntity(
    val id: String,
    val source: String,
    val title: String,
    val cover: String?,
    val author: String?,
    val updatedAt: String?
)

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun remove(id: String)

    @Query("SELECT * FROM bookmarks ORDER BY title ASC")
    fun observe(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    suspend fun isBookmarked(id: String): Boolean
}

@Entity(
    tableName = "history",
    indices = [Index(value = ["comicId", "chapterNumber"], unique = true)]
)
data class HistoryEntity(
    val comicId: String,
    val title: String,
    val source: String,
    val cover: String?,
    val chapterNumber: String,
    val progress: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun observe(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM history")
    suspend fun clear()
}
