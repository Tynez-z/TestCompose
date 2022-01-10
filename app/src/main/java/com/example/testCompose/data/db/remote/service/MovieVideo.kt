package com.example.testCompose.data.db.remote.service

object MovieVideo {
        private const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
        private const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/"

        @JvmStatic
        fun getYoutubeVideoPath(key: String?): String = "$YOUTUBE_VIDEO_URL$key"

        @JvmStatic
        fun getYoutubeThumbnailPath(thumbnailPath: String?): String {
            return "$YOUTUBE_THUMBNAIL_URL$thumbnailPath/default.jpg"
        }
    }