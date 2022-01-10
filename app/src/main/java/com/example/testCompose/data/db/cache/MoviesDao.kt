package com.example.testCompose.data.db.cache

import androidx.room.*
import com.example.testCompose.domain.entity.Movies

interface MoviesDao : MoviesCache {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override fun insert(movies: Movies)

    @Update
    override fun update(movies: Movies)

    @Query("SELECT * FROM movies")
    override fun getMovies(): List<Movies>

    @Delete
    override fun deleteMovies(movies: Movies)
}