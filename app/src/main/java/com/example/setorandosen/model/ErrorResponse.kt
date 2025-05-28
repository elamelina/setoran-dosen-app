package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: String?,

    @SerializedName("error_description")
    val error_description: String?
)