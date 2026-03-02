package com.example.testCompose.domain.entity.converters

import androidx.room.TypeConverter
import com.example.testCompose.domain.entity.review.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReviewListConverter {
    @TypeConverter
    fun reviewListToString(list: List<Result>): String? =
        Gson().toJson(list)

    @TypeConverter
    fun stringToReviewList(data: String?): List<Result> {
        if (data == null) {
            return listOf()
        }

        val arrayType = object : TypeToken<List<Result>>() {}.type
        return Gson().fromJson(data, arrayType)
    }
}