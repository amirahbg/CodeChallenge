package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.models.ResponseModel

interface RemoteDataSource {
    suspend fun getBrochures(): Result<ResponseModel?>
}