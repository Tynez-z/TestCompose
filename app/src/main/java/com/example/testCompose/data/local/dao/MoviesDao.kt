package com.example.testCompose.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    //Movies
    @Query("SELECT * FROM movie")
    fun getPagedMovies(): PagingSource<Int, Movies>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMovies(movies: List<Movies>)

    @Query("DELETE FROM movie")
    suspend fun clearAllMovies()

    // Returns true if at least one row exists.
    // SELECT EXISTS stops as soon as it finds a row → O(1)

    @Query("SELECT EXISTS (SELECT 1 FROM movie LIMIT 1)")
    suspend fun hasMovies(): Boolean

    //Favourites
    @Query("SELECT * FROM favoriteMovie")
    fun getAllFavouriteMovies(): Flow<List<SavedMovie>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addMovieToFavourites(movies: SavedMovie)

    @Delete
    suspend fun removeMovieFromFavourites(movie: SavedMovie)

    //Actors
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertActors(actors: List<Actors>)

    @Query("SELECT * FROM actor")
    fun getAllActorsFromDB(): Flow<List<Actors>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMovieActorsCrossRef(role: List<MovieActorsCrossRefTable>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertReviews(reviews: List<Result>)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//     suspend fun insertMovieReviews(movieReviews: Reviews)


    /**
     * find movies where actorId played
     *
    SELECT movies.* FROM movies
    INNER JOIN movieActorsCrossRefTable ON movieActorsCrossRefTable.movieId = movies.movieId
    WHERE movieActorsCrossRefTable.actorId = 109
     */

    /**
     * Show the list of movies which sorted rating based on average number of review on movie
     *
     * SELECT movie.*, AVG(review.rating) AS total_rating FROM movie
     * INNER JOIN review ON review.movieId = movie.movieId
     * GROUP BY movie.movieId
     * ORDER BY total_rating DESC
     */

    /**
     * Find all Reviews for a specific Actor (Multi-Table JOIN)
     * SELECT review.* FROM review
     *     INNER JOIN movie ON review.movieId = movie.movieId
     *     INNER JOIN movieActorsCrossRefTable ON movie.movieId = movieActorsCrossRefTable.movieId
     *     INNER JOIN actor ON movieActorsCrossRefTable.actorId = actor.actorId
     *     WHERE actor.name = "Chandler Lindauer"
     */
}