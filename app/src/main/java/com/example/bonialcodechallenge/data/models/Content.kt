package com.example.bonialcodechallenge.data.models

data class Content(
    val id: Long?,
    val content: List<Content>?,
    val contentType: String?,
    val brochureImage: String?,
    val distance: Double?,
    val retailer: Retailer?,
) {
    constructor(
        id: Long?,
        content: Content?,
        contentType: String?,
        brochureImage: String?,
        distance: Double?,
        retailer: Retailer?,
    ) : this(id, if (content != null) listOf(content) else null, contentType, brochureImage, distance, retailer)
}