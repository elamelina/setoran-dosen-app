// File: RiwayatSetoranResponse.kt
package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class RiwayatSetoranResponse(
    @SerializedName("response")
    val response: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<LogSetoran>
)
