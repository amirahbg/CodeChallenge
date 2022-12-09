package com.example.bonialcodechallenge.data.local

import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.main.BrochureFilter
import kotlinx.coroutines.flow.Flow

interface BrochureLocalDataSource {

    suspend fun saveBrochures(brochures: List<Brochure>)

    suspend fun getBrochures(filter: BrochureFilter): Flow<List<Brochure>>

}