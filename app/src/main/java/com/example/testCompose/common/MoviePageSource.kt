package com.example.testCompose.common

import android.util.Log
import androidx.compose.runtime.internal.illegalDecoyCallException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testCompose.data.db.remote.service.ApiMovies
import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.Movies
import com.example.testCompose.domain.interactor.useCase.GetMoviesUseCase
import retrofit2.HttpException

class MoviePageSource(
    private val getMoviesUseCase: GetMoviesUseCase,
) : PagingSource<Int, Movies>() {

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {
            val nextPage = params.key ?: 1
            val totalPages: Int = params.loadSize.coerceAtMost(ApiMovies.MAX_PAGE_SIZE)
            val response = getMoviesUseCase.execute(nextPage, totalPages)

            LoadResult.Page(
                data = response.body()!!.results,
                prevKey = if (nextPage == 1) null else nextPage -1,
                nextKey = if(response.body()!!.results.size < totalPages) null else nextPage + 1
//                nextKey = if (response.body() == null) null else response.body()!!.page.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}