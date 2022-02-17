package com.example.testCompose.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testCompose.data.db.remote.service.ApiMovies
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.entity.detailMovie.MovieDetails
import com.example.testCompose.domain.entity.search.ResultSearchMovie
import com.example.testCompose.domain.entity.search.SearchMovies
import com.example.testCompose.domain.interactor.useCase.GetSearchMovieUseCase

class SearchMoviePageSource(
    private val getSearchMovieUseCase: GetSearchMovieUseCase,
    private val query: String
) : PagingSource<Int, MovieDetails>() {
    override fun getRefreshKey(state: PagingState<Int, MovieDetails>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetails> {
        return try {
            val nextPage = params.key ?: 1
            val totalPages: Int = params.loadSize.coerceAtMost(ApiMovies.MAX_PAGE_SIZE)
            val response = getSearchMovieUseCase.execute(nextPage, query = query)

            LoadResult.Page(
                data = response.body()!!.results,
                prevKey = if (nextPage == 1) null else nextPage -1,
                nextKey = if(response.body()!!.page > totalPages) null else nextPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}