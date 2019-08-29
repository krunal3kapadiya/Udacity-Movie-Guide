package com.krunal3kapadiya.popularmovies.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

data class RequestTokenResponse(@SerializedName("success") val success: Boolean,
                                @SerializedName("expires_at") val expiresAt: String,
                                @SerializedName("request_token") val requestToken: String)

data class LoginResponse(@SerializedName("status_code") val statusCode: Int,
                         @SerializedName("status_message") val statusMessage: Int)