package com.example.bonialcodechallenge.data.remote

import com.example.bonialcodechallenge.data.models.ResponseModel
import javax.inject.Inject

class BrochureRemoteDataSourceImpl @Inject constructor(private val bonialService: BonialService) :
    BrochureRemoteDataSource {

    override suspend fun getBrochures(): Result<ResponseModel?> {
        return try {
            val response = bonialService.getBrochures()
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Throwable(response.message()))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}