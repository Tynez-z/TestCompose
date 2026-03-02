package com.example.testCompose.domain.entity.converters

import androidx.room.TypeConverter
import com.example.testCompose.domain.entity.actors.Actors
import com.example.testCompose.domain.entity.review.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieActorsListConverter {
    @TypeConverter
    fun movieActorsListToString(list: List<Actors>): String? =
        Gson().toJson(list)

    @TypeConverter
    fun stringToMovieActors(data: String?): List<Actors> {
        if (data == null) {
            return listOf()
        }

        val arrayType = object : TypeToken<List<Actors>>() {}.type
        return Gson().fromJson(data, arrayType)
    }
}