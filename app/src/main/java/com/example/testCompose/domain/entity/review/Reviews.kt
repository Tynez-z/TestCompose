package com.example.testCompose.domain.entity.review

data class Reviews(
    val id: Int,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)