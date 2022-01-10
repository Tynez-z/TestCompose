package com.example.testCompose.data.db.cache

import com.example.testCompose.domain.entity.Movies

interface MoviesCache {
    fun getMovies(): List<Movies>
    fun insert(movies: Movies)
    fun update (movies: Movies)
    fun deleteMovies(movies: Movies)
}