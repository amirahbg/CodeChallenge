package com.example.bonialcodechallenge.data.models

data class Brochure(
    val id: Long,
    val brochureImageUrl: String,
    val retailerName: String,
    val isPremium: Boolean,
    val distance: Double
)