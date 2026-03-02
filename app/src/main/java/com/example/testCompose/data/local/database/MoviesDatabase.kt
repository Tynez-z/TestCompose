package com.example.testCompose.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testCompose.data.local.dao.MoviesDao
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.actors.MovieActorsCrossRefTable
import com.example.testCompose.domain.entity.converters.AuthorDetailsConverter
import com.example.testCompose.domain.entity.converters.GenreListConverter
import com.example.testCompose.domain.entity.converters.IntegerListConverter
import com.example.testCompose.domain.entity.converters.MovieActorsListConverter
import com.example.testCompose.domain.entity.converters.ReviewListConverter
import com.example.testCompose.domain.entity.review.Result
import com.example.testCompose.domain.entity.savedMovies.SavedMovie

@Database(
    entities = [
        SavedMovie::class,
        Movies::class,
        Actors::class,
        MovieActorsCrossRefTable::class,
        Result::class],
    version = 42,
    exportSchema = false
)
@TypeConverters(
    value = [
        GenreListConverter::class,
        ReviewListConverter::class,
        IntegerListConverter::class,
        MovieActorsListConverter::class,
        AuthorDetailsConverter::class]
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
}