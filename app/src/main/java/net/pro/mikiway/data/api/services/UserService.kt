package net.pro.mikiway.data.api.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


/*
* Define response api
* - signup:
*          1. email otp
*          2. user authentication
*             - user info
*             - access token & refresh token
* - login:
*
*
* */

//signup
data class Metadata(
    val code: Int,
    val message: String,
    val metadata: OtpEmail
)

  //1
data class OtpEmail(
    val _id: String,
    val usr_email: String,
    val isEmailVerified: Boolean
)
//2
data class MetadataToken(
    val code: Int,
    val message: String,
    val metadata: MetadataAuth
)
//2.1
data class MetadataAuth(
    val user : UserToken,
    val tokens : TokenUser
)
//2.1.1
data class UserToken (
    val _id: String,
    val usr_name: String,
    val usr_email : String
)
//2.1.1
data class TokenUser (
    val accessToken: String,
    val refreshToken: String
)

//login



//call api response
data class EmailRequest(val email: String)
data class TokenRequest(val token: String)
data class PasswordRequest(val email: String,val username: String, val password: String)
data class ApiResponse<T>(val status: Int, val message: String, val metadata: T?)

interface UserService {

    @POST("v1/api/user/new_user")
     fun registerUser(@Body request: EmailRequest): Call<ApiResponse<Any>>

    @GET("v1/api/user/welcome-back")
     fun checkOtp(@Query("token") token: String): Call<ApiResponse<Metadata>>

    @POST("v1/api/user/signup")
     fun signup(@Body request: PasswordRequest): Call<ApiResponse<MetadataToken>>
}