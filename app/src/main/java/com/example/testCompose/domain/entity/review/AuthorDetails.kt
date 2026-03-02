package com.example.testCompose.domain.entity.review

data class AuthorDetails(
    val avatar_path: String?,
    val name: String,
    val rating: Int,
    val username: String
)