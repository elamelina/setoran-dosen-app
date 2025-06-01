package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class SetoranResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<Setoran>
)