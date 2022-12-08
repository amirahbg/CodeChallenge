package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.models.ResponseModel
import javax.inject.Inject

class DefaultRemoteDataSource @Inject constructor(private val bonialService: BonialService) : RemoteDataSource {

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