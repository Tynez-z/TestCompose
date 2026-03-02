package com.example.testCompose.domain.entity.review

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.testCompose.domain.entity.Movies

@Entity(
    tableName = "review",
    foreignKeys = [
        ForeignKey(
            entity = Movies::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["movieId"])]
)
data class Result(
    @PrimaryKey
    val id: String,
    val movieId: Int,
    val author: String,
    val rating: Int,
    val author_details: AuthorDetails,
    val content: String,
    val created_at: String,
    val updated_at: String,
    val url: String,
)