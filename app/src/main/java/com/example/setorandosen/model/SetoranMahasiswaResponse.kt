package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class MahasiswaSetoranResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<MahasiswaSetoran>
)