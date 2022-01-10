package com.example.testCompose.domain.entity.video

import com.example.testCompose.data.db.remote.service.MovieVideo

data class Video(
    val id: String,
    val iso_3166_1: String,
    val iso_639_1: String,
    val key: String,
    val name: String,
    val official: Boolean,
    val published_at: String,
    val site: String,
    val size: Int,
    val type: String,
    val url : String = MovieVideo.getYoutubeVideoPath(key)
)