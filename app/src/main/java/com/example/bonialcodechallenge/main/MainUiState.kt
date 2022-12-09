package com.example.bonialcodechallenge.main

import com.example.bonialcodechallenge.data.models.Brochure

sealed class MainUiState {
    data class Success(val brochures: List<Brochure>) : MainUiState()
    object Empty : MainUiState()
    object Loading : MainUiState()
    data class Error(val exception: Throwable) : MainUiState()
}