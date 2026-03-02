package com.example.testCompose.domain.entity.review

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "MovieReviewCrossRef",
    primaryKeys = ["movieId", "reviewId"],
    indices = [Index("reviewId")]
)
data class MovieReviewCrossRef(
    val movieId: Int,
    val reviewId: String
)