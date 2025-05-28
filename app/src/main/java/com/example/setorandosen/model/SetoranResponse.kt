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

data class Setoran(
    @SerializedName("id")
    val id: String,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("jenis_setoran")
    val jenisSetoran: String,

    @SerializedName("jumlah_ayat")
    val jumlahAyat: Int,

    @SerializedName("keterangan")
    val keterangan: String?
)