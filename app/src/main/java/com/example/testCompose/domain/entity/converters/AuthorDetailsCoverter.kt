package com.example.testCompose.domain.entity.converters

import androidx.room.TypeConverter
import com.example.testCompose.domain.entity.detailMovie.Genre
import com.example.testCompose.domain.entity.review.AuthorDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AuthorDetailsConverter {
    @TypeConverter
    fun authorDetailsToString(authorDetail: AuthorDetails): String? =
        Gson().toJson(authorDetail)

    @TypeConverter
    fun stringToAuthorDetailsList(data: String?): AuthorDetails? {
        if (data == null) {
            return null
        }

        val arrayType = object : TypeToken<AuthorDetails>() {}.type
        return Gson().fromJson(data, arrayType)
    }
}