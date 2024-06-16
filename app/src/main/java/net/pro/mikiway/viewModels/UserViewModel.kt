package net.pro.mikiway.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.pro.mikiway.data.api.services.EmailRequest
import net.pro.mikiway.data.api.services.PasswordRequest
import net.pro.mikiway.data.api.services.TokenRequest
import net.pro.mikiway.data.api.services.UserService
import retrofit2.awaitResponse
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val apiService: UserService
) : ViewModel() {

    fun registerUser(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.registerUser(EmailRequest(email)).execute()
                }
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == 200) {
                        onResult(true, "Verification email sent")
                    } else {
                        onResult(false, responseBody?.message ?: "Error")
                    }
                } else {
                    onResult(false, "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("RegisterUser", "Exception: ${e.message}", e)
                onResult(false, e.message ?: "Exception occurred")
            }
        }
    }

    fun verifyEmailToken(token: String, onResult: (Boolean, String, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.checkOtp(token).execute()
                }
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("respond", responseBody.toString())
                    }
                    if (responseBody?.status == 200) {
                        val email = responseBody.metadata?.metadata?.usr_email
                        onResult(true, "Token verified successfully", email)
                    } else {
                        onResult(false, responseBody?.message ?: "Error: Invalid status", null)
                    }
                } else {
                    onResult(false, "Error: ${response.message()}", null)
                }
            } catch (e: Exception) {
                Log.e("verifyEmailToken", "Exception occurred", e)
                onResult(false, e.message ?: "Exception occurred", null)
            }
        }
    }

    fun completeRegistration(
        email: String,
        username: String,
        password: String,
        onResult: (Boolean, String, Any?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.signup(PasswordRequest(email, username, password)).execute()
                }
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == 201) {
                        onResult(true, "Registration complete",
                            response.body()!!.metadata?.metadata?.user
                        )
                    } else {
                        onResult(false, responseBody?.message ?: "Error", null)
                    }
                } else {
                    onResult(false, "Error: ${response.message()}", null)
                }
            } catch (e: Exception) {
                Log.e("completeRegistration", "Exception: ${e.message}", e)
                onResult(false, e.message ?: "Exception occurred", null)
            }
        }
    }
}
