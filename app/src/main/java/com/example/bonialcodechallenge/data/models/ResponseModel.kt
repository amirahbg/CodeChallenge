package com.example.bonialcodechallenge.data.models

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("_embedded") val embedded: Embedded
)