package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.data.models.filterBrochures
import com.example.bonialcodechallenge.utils.UnexpectedError
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {
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