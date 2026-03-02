package com.example.testCompose.data.repository

import androidx.paging.PagingData
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.MoviesResponse
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.actors.MovieActors
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.genres.Genres
import com.example.testCompose.domain.entity.language.Languages
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.review.Reviews
import com.example.testCompose.domain.entity.savedMovies.SavedMovie
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMovies
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.type.Either
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesRepository {
    suspend fun getPagedMovies(): Flow<PagingData<Movies>>

    suspend fun getMovies(pageNumber: Int): Response<MoviesResponse>
    suspend fun getSearchMovies(pageNumber: Int, query: String): Response<SearchMovies>
    suspend fun getSimilarMovies(pageNumber: Int, movieId: Int): Response<SimilarMovies>
    fun getMovieDetails(movieId: Int): Either<Failure, MovieDetails>
    fun getMovieVideo(movieId: Int): Either<Failure, VideoList>
    fun getReviews(movieId: Int): Either<Failure, Reviews>
    suspend fun getLanguage(): Languages
    fun getGenres() : Either<Failure,Genres>

    suspend fun getMovieActors(movieId: Int): Either<Failure, MovieActors>

    //Room
    fun getAllFavoriteMovies(): Flow<List<SavedMovie>>
    suspend fun saveFavouriteMovie(movie: SavedMovie)
    suspend fun removeMovieFromFavourites(movie: SavedMovie)

//    suspend fun insertMovieActors(actors: MovieActors)
    suspend fun insertActors(actors: List<Actors>)
    suspend fun insertRoles(role: List<MovieActorsCrossRefTable>)

     fun getAllActorsFromDB(): Flow<List<Actors>>

    suspend fun insertReview(reviews: List<Result>)

//    suspend fun insertMovieReviews(movieReviews: Reviews)
}