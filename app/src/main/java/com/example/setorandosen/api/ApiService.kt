package com.example.setorandosen.api

import com.example.setorandosen.model.PaMahasiswaResponse
import com.example.setorandosen.model.MahasiswaSetoranResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("dosen/pa-saya")
    suspend fun getPaMahasiswa(
        @Header("Authorization") bearer: String,
        @Header("Accept") accept: String = "application/json"
    ): Response<PaMahasiswaResponse>

    @GET("mahasiswa/setoran/{nim}")
    suspend fun getSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String
    ): Response<MahasiswaSetoranResponse>
}