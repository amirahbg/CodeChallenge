package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.local.BrochureLocalDataSource
import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.data.models.toBrochures
import com.example.bonialcodechallenge.data.remote.BrochureRemoteDataSource
import com.example.bonialcodechallenge.main.BrochureFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BrochureRepositoryImpl @Inject constructor(
    private val brochureLocalDataSource: BrochureLocalDataSource,
    private val brochureRemoteDataSource: BrochureRemoteDataSource
) : BrochureRepository {

    override suspend fun fetchBrochures(): Flow<Result<Unit>> = flow {
        val result = brochureRemoteDataSource.getBrochures()
        result
            .onFailure {
                emit(Result.failure(it))
            }
            .onSuccess {
                it?.also {
                    val fetchedBrochures = it.toBrochures()
                    brochureLocalDataSource.saveBrochures(fetchedBrochures)
                }
                emit(Result.success(Unit))
            }
    }

    override suspend fun getBrochures(filter: BrochureFilter): Flow<List<Brochure>> {
        return brochureLocalDataSource.getBrochures(filter)
    }

}