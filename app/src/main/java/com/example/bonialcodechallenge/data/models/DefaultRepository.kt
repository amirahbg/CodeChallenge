package com.example.bonialcodechallenge.data.models

import com.example.bonialcodechallenge.data.RemoteDataSource
import com.example.bonialcodechallenge.data.Repository
import com.example.bonialcodechallenge.utils.UnexpectedError
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) : Repository {
    override suspend fun getBrochures(): Result<List<Brochure>?> {
        val result = remoteDataSource.getBrochures()

        result
            .onFailure {
                return Result.failure(it)
            }
            .onSuccess {
                return Result.success(it?.filterBrochures())
            }
        return Result.failure(UnexpectedError())
    }
}