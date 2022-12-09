package com.example.bonialcodechallenge.data.remote

import com.example.bonialcodechallenge.data.models.ResponseModel

interface BrochureRemoteDataSource {
    suspend fun getBrochures(): Result<ResponseModel?>
}