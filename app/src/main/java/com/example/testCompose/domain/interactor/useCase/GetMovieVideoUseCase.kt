package com.example.testCompose.domain.interactor.useCase

import com.example.testCompose.data.repository.MoviesRepository
import com.example.testCompose.domain.entity.video.VideoList
import com.example.testCompose.domain.exception.Failure
import com.example.testCompose.domain.interactor.UseCase
import com.example.testCompose.domain.type.Either
import javax.inject.Inject

class GetMovieVideoUseCase @Inject constructor(private val repository: MoviesRepository) :
    UseCase<VideoList, GetMovieVideoUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, VideoList> =
        repository.getMovieVideo(params.movieId)


    data class Params(val movieId: Int)
}