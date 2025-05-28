package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class PaMahasiswa(
    @SerializedName("nim")
    val nim: String,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("prodi")
    val prodi: String?,

    @SerializedName("angkatan")
    val angkatan: String?,

    @SerializedName("total_setoran")
    val totalSetoran: Int = 0,

    @SerializedName("last_setoran")
    val lastSetoran: String?
)

data class PaMahasiswaResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<PaMahasiswa>
)