package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.models.Brochure

interface Repository {
    suspend fun getBrochures(): Result<List<Brochure>?>
}