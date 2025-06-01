package com.example.setorandosen.api

import com.example.setorandosen.model.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token")
    suspend fun login(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String
    ): Response<LoginResponse>
}