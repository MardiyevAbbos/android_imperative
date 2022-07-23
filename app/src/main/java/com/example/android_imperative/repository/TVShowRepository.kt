package com.example.android_imperative.repository

import com.example.android_imperative.db.TVShowDao
import com.example.android_imperative.model.TVShow
import com.example.android_imperative.network.TVShowService
import javax.inject.Inject

class TVShowRepository @Inject constructor(
    private val tvShowService: TVShowService,
    private val tvShowDao: TVShowDao
) {

    /**
     * Retrofit Related
     */
    suspend fun apiTVShowPopular(page: Int) = tvShowService.apiTVShowPopular(page)
    suspend fun apiTVShowDetails(q: Int) = tvShowService.apiTVShowDetails(q)

    /**
     * Room Related
     */
    suspend fun getTVShowsFromDB() = tvShowDao.getTVShowFromDB()
    suspend fun insertTVShowToDB(tvShow: TVShow) = tvShowDao.insertTvShowToDB(tvShow)
    suspend fun removeTVShowFromDB(id: Long) = tvShowDao.removeTvShowFromDB(id)
    suspend fun deleteTVShowsFromDB() = tvShowDao.deleteTVShowsFromDB()

}