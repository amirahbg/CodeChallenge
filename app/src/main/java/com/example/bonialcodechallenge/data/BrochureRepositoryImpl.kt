package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.local.BrochureLocalDataSource
import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.data.models.toBrochures
import com.example.bonialcodechallenge.data.remote.BrochureRemoteDataSource
import com.example.bonialcodechallenge.main.BrochureFilter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BrochureRepositoryImpl @Inject constructor(
    private val brochureLocalDataSource: BrochureLocalDataSource,
    private val brochureRemoteDataSource: BrochureRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : BrochureRepository {

    override suspend fun fetchBrochures(): Flow<Result<Unit>> = flow<Result<Unit>> {
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
    }.flowOn(ioDispatcher)

    override suspend fun getBrochures(filter: BrochureFilter): Flow<List<Brochure>> {
        return brochureLocalDataSource.getBrochures(filter)
    }

}