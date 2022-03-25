package com.example.testCompose.domain.entity.converters

import androidx.room.TypeConverter
import com.example.testCompose.domain.entity.detailMovie.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreListConverter {

    @TypeConverter
    fun genreListToString(list: List<Genre>): String? =
        Gson().toJson(list)

    @TypeConverter
    fun stringToSGenreList(data: String?): List<Genre> {
        if (data == null) {
            return listOf()
        }

        val arrayType = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(data, arrayType)
    }
}