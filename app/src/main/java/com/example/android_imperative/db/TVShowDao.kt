package com.example.android_imperative.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_imperative.model.TVShow

@Dao
interface TVShowDao {
    @Query("SELECT * FROM tv_show")
    suspend fun getTVShowFromDB(): List<TVShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShowToDB(tvShow: TVShow)

    @Query("DELETE FROM tv_show WHERE id=:movieId")
    fun removeTvShowFromDB(movieId: Long)

    @Query("DELETE FROM tv_show")
    suspend fun deleteTVShowsFromDB()
}