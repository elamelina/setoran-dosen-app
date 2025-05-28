package com.example.setorandosen.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val access_token: String,

    @SerializedName("token_type")
    val token_type: String,

    @SerializedName("expires_in")
    val expires_in: Int,

    @SerializedName("refresh_token")
    val refresh_token: String?,

    @SerializedName("refresh_expires_in")
    val refresh_expires_in: Int?,

    @SerializedName("not-before-policy")
    val not_before_policy: Int?,

    @SerializedName("session_state")
    val session_state: String?,

    @SerializedName("scope")
    val scope: String?
)