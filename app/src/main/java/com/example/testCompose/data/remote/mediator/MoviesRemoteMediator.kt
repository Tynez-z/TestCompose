package com.example.testCompose.data.remote.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.testCompose.data.local.datasource.MoviesLocalDataSource
import com.example.testCompose.data.remote.datasource.MoviesRemoteDataSource
import com.example.testCompose.domain.entity.Movies
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator @Inject constructor(
    private val moviesRemote: MoviesRemoteDataSource,
    private val moviesLocal: MoviesLocalDataSource
) : RemoteMediator<Int, Movies>() {

    private var currentPage = 0
    private var totalPages = Int.MAX_VALUE
    private val isLoading = AtomicBoolean(false)
    private val TAG = "MoviesRemoteMediator"


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movies>
    ): MediatorResult {
        // Prevent concurrent loads of the same page
        if (isLoading.compareAndSet(false, true) && loadType != LoadType.REFRESH) {
            return MediatorResult.Success(endOfPaginationReached = false)
        }

        return try {
//            isLoading = true

            val page = when (loadType) {
                // REFRESH – first time the list is built or user pulls‑to‑refresh
                LoadType.REFRESH -> {
                    // Check the DB *right now*. If there is at least one row,
                    // we can skip the network call and simply use the cached data.
                    val dbHasMovies = moviesLocal.hasMovies()
                    Log.d(TAG, "REFRESH – DB has movies = $dbHasMovies")

                    if (dbHasMovies) {
                        // No need to hit the API – just tell Paging we’re done.
                        Log.d(TAG, "Skipping network – using cached DB data")
                        currentPage = 0          // reset counters for future APPEND
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }

                    // DB empty -> fetch the first page from the server
                    Log.d(TAG, "DB empty – fetching page 1")
                    currentPage = 0
                    totalPages = Int.MAX_VALUE
                    1
                }

                LoadType.PREPEND -> {
                    Log.d(TAG, "PREPEND - not supported")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    if (currentPage >= totalPages) {
                        Log.d(TAG, "End reached: currentPage=$currentPage, totalPages=$totalPages")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    val nextPage = currentPage + 1
                    Log.d(TAG, "APPEND - loading page $nextPage (current: $currentPage)")
                    nextPage
                }
            }

            Log.d(TAG, "Fetching page: $page")

            // Fetch from network
            val response = moviesRemote.getMovies(pageNumber = page)
            if (!response.isSuccessful) {
                Log.e(TAG, "HTTP error: ${response.code()}")
                return MediatorResult.Error(Exception("HTTP ${response.code()}"))
            }

            val body = response.body()
                ?: return MediatorResult.Error(Exception("Empty response"))

            totalPages = body.total_pages
            val movies = body.results.map { it.copy(page = page) }

            Log.d(TAG, "Loaded ${movies.size} movies for page $page (totalPages: $totalPages)")

            // Save to database
            if (loadType == LoadType.REFRESH) {
                moviesLocal.clearAllMovies()
            }
            /**
             * # Sync routine
             * 1. start transaction
             * 2. iterate over movie list
             * 3. for each movie, gather related info (actors, reviews, ...)
             * 4. after info is gathered call:
             *   4.a moviesCache.insertMovie
             *   4.b actorsCache.<...>
             *   4.c reviewCache.<...>
             *   <...>
             * 5. commit the transaction, catch errors
             */
            moviesLocal.insertMovies(movies)

            // Update current page ONLY after successful save
            currentPage = page
            Log.d(TAG, "Successfully loaded page $page, updated currentPage to: $currentPage")

            val endOfPagination = movies.isEmpty() || page >= totalPages

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading page", e)
            MediatorResult.Error(e)
        } finally {
            isLoading.set(false)
        }
    }
}