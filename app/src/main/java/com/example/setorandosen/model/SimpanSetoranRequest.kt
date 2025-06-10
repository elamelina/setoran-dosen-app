package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class SimpanSetoranRequest(
    @SerializedName("data_setoran")
    val dataSetoran: List<DataSetoranItem>,
    @SerializedName("tgl_setoran")
    val tglSetoran: String? = null,
    val keterangan: String? = null
)
