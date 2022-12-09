package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.main.BrochureFilter
import kotlinx.coroutines.flow.Flow

interface BrochureRepository {

    suspend fun fetchBrochures(): Flow<Result<Unit>>

    suspend fun getBrochures(filter: BrochureFilter): Flow<List<Brochure>>

}