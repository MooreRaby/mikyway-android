package net.pro.mikiway.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.otpview.OTPListener
import com.otpview.OTPTextView
import dagger.hilt.android.AndroidEntryPoint
import net.pro.mikiway.R
import net.pro.mikiway.databinding.ActivitySendOtpBinding
import net.pro.mikiway.viewModels.UserViewModel

@AndroidEntryPoint
class SendOtpActivity : AppCompatActivity() {
    private lateinit var otpTextView: OTPTextView
    private lateinit var binding: ActivitySendOtpBinding
    private val userViewModel: UserViewModel by viewModels()
    private var countRunning: Boolean = true
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivitySendOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("EMAIL")
        binding.tvEmail.text = email

        otpTextView = binding.otpView
        otpTextView.requestFocusOTP()
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {}

            override fun onOTPComplete(otp: String) {
            }
        }

        binding.btnCheckOtp.setOnClickListener {
            if (email == null) {
                return@setOnClickListener
            }
            if (!countRunning) {
                userViewModel.registerUser(email) { success, message ->
                    Log.i("success1", success.toString())
                    if (success) {
                        Toast.makeText(this, "Resend otp", Toast.LENGTH_SHORT).show()
                        countRunning = true
                        //count down otp
                        val startTimeInMillis: Long = 300000
                        startCountDown(startTimeInMillis)
                    }
                }
            } else {
                val otp = binding.otpView.otpEditText?.text.toString().trim()
                if (otp.isEmpty()) {
                    Toast.makeText(this, "OTP không được bỏ trống", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (otp.length != 6) {
                    Toast.makeText(this, "OTP gồm 6 ký tự", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Log.i("token", otp)
                userViewModel.verifyEmailToken(otp) { success, message, email ->
                    if (success && email != null) {
                        Log.i("email", email)
                        Log.i("info respond", "$success $email $message")
                        // Chuyển đến màn hình đặt mật khẩu
                        val intent = Intent(this, SignupCompleteActivity::class.java)
                        intent.putExtra("EMAIL", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Otp không chính xác", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        binding.tvResendOtp.setOnClickListener {
            if (email == null) {
                return@setOnClickListener
            }
            if (countRunning) {
                countDownTimer.cancel()
                userViewModel.registerUser(email) { success, message ->
                    Log.i("success1", success.toString())
                    if (success) {
                        Toast.makeText(this, "Resend otp", Toast.LENGTH_SHORT).show()
                        countRunning = true
                        //count down otp
                        val startTimeInMillis: Long = 300000
                        startCountDown(startTimeInMillis)
                    }
                }
            }

        }

        binding.btnBack.setOnClickListener {
            finish()
        }


        // setup pin view
        additionalMethods()

        //count down otp
        val startTimeInMillis: Long = 300000
        startCountDown(startTimeInMillis)

    }

    private fun startCountDown(startTimeInMillis: Long) {
        countDownTimer = object : CountDownTimer(startTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                val timeFormatted = String.format("%02d:%02d", minutes, seconds)
                binding.btnCheckOtp.text = "XÁC MINH ($timeFormatted)"
            }

            override fun onFinish() {
                // Hành động khi đếm ngược hoàn thành
                binding.btnCheckOtp.text = "Gửi lại"
                countRunning = false
            }
        }
        countDownTimer.start()
    }


    fun additionalMethods() {
        otpTextView.otpListener  // retrieves the current OTPListener (null if nothing is set)
        otpTextView.requestFocusOTP();    //sets the focus to OTP box (does not open the keyboard)
        otpTextView.otp;    // retrieves the OTP entered by user (works for partial otp input too)
        otpTextView.showSuccess();    // shows the success state to the user (can be set a bar color or drawable)
        otpTextView.showError();    // shows the success state to the user (can be set a bar color or drawable)
        otpTextView.resetState();    // brings the views back to default state (the state it was at input)
    }
}