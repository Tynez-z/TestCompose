package com.example.testCompose.data.db.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.converters.GenreListConverter
import com.example.testCompose.domain.entity.detailMovie.MovieDetails

@Database(entities = [MovieDetails::class], version = 7 , exportSchema = false)
@TypeConverters(value = [(GenreListConverter:: class)])
abstract class MoviesDatabase : RoomDatabase() {

    abstract val moviesDao: MoviesDao

    companion object {

        @Volatile
        private var instance: MoviesDatabase? = null

        fun getInstance(context: Context): MoviesDatabase {
            var instance = instance

            if (instance == null) {
                instance = Room
                    .databaseBuilder(context.applicationContext, MoviesDatabase::class.java, "movies_database")
                    .fallbackToDestructiveMigration()
                    .build()
                MoviesDatabase.instance = instance
            }

            return instance
        }
    }
}