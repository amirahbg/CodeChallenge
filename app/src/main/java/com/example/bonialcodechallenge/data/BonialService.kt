package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.models.ResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface BonialService {

    @GET("stories-test/shelf.json")
    suspend fun getBrochures(): Response<ResponseModel>
}