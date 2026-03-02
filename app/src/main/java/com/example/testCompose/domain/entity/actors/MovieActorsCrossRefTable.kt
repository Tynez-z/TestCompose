package com.example.testCompose.domain.entity.actors

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.testCompose.domain.entity.Movies

@Entity(
    tableName = "movieActorsCrossRefTable",
    foreignKeys = [
        ForeignKey(
            entity = Movies::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"],
            onDelete = CASCADE

        ),
        ForeignKey(
            entity = Actors::class,
            parentColumns = ["actorId"],
            childColumns = ["actorId"]
        )
    ],
    indices = [
        Index(value = ["movieId"]),
        Index(value = ["actorId"])
    ]
)
data class MovieActorsCrossRefTable(
    @PrimaryKey(autoGenerate = true)
    val roleId: Int = 0,
    val movieId: Int,
    val actorId: Int
)