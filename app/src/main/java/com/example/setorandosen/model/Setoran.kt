package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

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