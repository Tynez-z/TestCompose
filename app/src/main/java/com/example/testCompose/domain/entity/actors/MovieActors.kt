package com.example.testCompose.domain.entity.actors

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class MovieActors (
    val id: Int,
    val cast: List<Actors>,
)