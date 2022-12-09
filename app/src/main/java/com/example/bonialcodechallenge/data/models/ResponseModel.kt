package com.example.bonialcodechallenge.data.models

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("_embedded") val embedded: Embedded
)

fun ResponseModel.toBrochures(): List<Brochure> {
    return embedded.contents
        .filterNotNull()
        .filter { it.contentType != null && (it.contentType == "brochure" || it.contentType == "brochurePremium") }
        .map {
            Brochure(
                it.content?.first()?.id ?: 0,
                it.content?.first()?.brochureImage ?: "",
                it.content?.first()?.retailer?.name ?: "",
                it.contentType == "brochurePremium",
                it.content?.first()?.distance ?: Double.MAX_VALUE
            )
        }
}