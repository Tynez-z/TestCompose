package com.example.testCompose.domain.entity.actors

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actor")
data class Actors(
    @PrimaryKey
    @ColumnInfo("actorId")
    val id: Int,
    val name: String,
    val character: String,
    val popularity: Float,
    val profile_path: String? = null
)