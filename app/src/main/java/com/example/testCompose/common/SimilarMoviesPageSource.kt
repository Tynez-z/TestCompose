package com.example.testCompose.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testCompose.data.db.remote.service.ApiMovies
import com.example.testCompose.domain.entity.similarMovies.SimilarMoviesItems
import com.example.testCompose.domain.interactor.useCase.GetSimilarMoviesUseCase

class SimilarMoviesPageSource(
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val movieId: Int
) : PagingSource<Int, SimilarMoviesItems>() {
    override fun getRefreshKey(state: PagingState<Int, SimilarMoviesItems>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SimilarMoviesItems> {
        return try {
            val nextPage = params.key ?: 1
            val totalPages: Int = params.loadSize.coerceAtMost(ApiMovies.MAX_PAGE_SIZE)
            val response = getSimilarMoviesUseCase.execute(nextPage, movieId = movieId)

            LoadResult.Page(
                data = response.body()!!.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.body()!!.page > totalPages) null else nextPage + 1
//                nextKey = if (response.body() == null) null else response.body()!!.page.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}