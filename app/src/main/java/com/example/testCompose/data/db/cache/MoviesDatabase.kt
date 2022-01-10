package com.example.testCompose.data.db.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testCompose.domain.entity.Movies

@Database(entities = [Movies::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {


}