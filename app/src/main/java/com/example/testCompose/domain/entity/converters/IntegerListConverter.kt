package com.example.testCompose.domain.entity.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntegerListConverter {
    @TypeConverter
    fun integerListToString(list: List<Int>): String? =
        Gson().toJson(list)

    @TypeConverter
    fun stringToIntegerList(data: String?): List<Int> {
        if (data == null) {
            return listOf()
        }

        val arrayType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(data, arrayType)
    }
}