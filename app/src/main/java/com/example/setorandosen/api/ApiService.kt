// Updated ApiService.kt
package com.example.setorandosen.api

import com.example.setorandosen.model.*
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("dosen/pa-saya")
    suspend fun getPaMahasiswa(
        @Header("Authorization") bearer: String,
        @Header("Accept") accept: String = "application/json"
    ): Response<PaMahasiswaResponse>

    @GET("mahasiswa/setoran/{nim}")
    suspend fun getDetailSetoranMahasiswa(
        @Header("Authorization") token: String,
        @Path("nim") nim: String
    ): Response<DetailSetoranResponse>

    @POST("mahasiswa/setoran/{nim}")
    suspend fun simpanSetoran(
        @Header("Authorization") token: String,
        @Path("nim") nim: String
    ): Response<GenericResponse>
}
