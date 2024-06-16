package net.pro.mikiway.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.otpview.OTPListener
import com.otpview.OTPTextView
import dagger.hilt.android.AndroidEntryPoint
import net.pro.mikiway.R
import net.pro.mikiway.databinding.FragmentSendOtpBinding
import net.pro.mikiway.viewModels.UserViewModel

@AndroidEntryPoint
class SendOtpFragment : Fragment() {

    private lateinit var binding: FragmentSendOtpBinding
    private lateinit var otpTextView: OTPTextView
    private val userViewModel: UserViewModel by viewModels()
    private var countRunning: Boolean = false
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendOtpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("EMAIL")
        binding.tvEmail.text = email

        otpTextView = binding.otpView
        otpTextView.requestFocusOTP()
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {}
        }

        binding.btnCheckOtp.setOnClickListener {
            handleCheckOtpClick(email)
        }

        binding.tvResendOtp.setOnClickListener {
            handleResendOtpClick(email)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // setup pin view
        additionalMethods()

        // Start the initial countdown
        startCountDown(300000)
    }

    private fun handleCheckOtpClick(email: String?) {
        if (email == null) {
            return
        }
        if (!countRunning) {
            userViewModel.registerUser(email) { success, message ->
                Log.i("success1", success.toString())
                if (success) {
                    Toast.makeText(requireContext(), "Resend otp", Toast.LENGTH_SHORT).show()
                    startCountDown(300000)
                }
            }
        } else {
            val otp = binding.otpView.otpEditText?.text.toString().trim()
            if (otp.isEmpty()) {
                Toast.makeText(requireContext(), "OTP không được bỏ trống", Toast.LENGTH_SHORT)
                    .show()
                return
            }
            if (otp.length != 6) {
                Toast.makeText(requireContext(), "OTP gồm 6 ký tự", Toast.LENGTH_SHORT).show()
                return
            }
            Log.i("token", otp)
            userViewModel.verifyEmailToken(otp) { success, message, email ->
                if (success && email != null) {
                    Log.i("email", email)
                    Log.i("info respond", "$success $email $message")
                    // Navigate to SignupCompleteFragment
                    val signupCompleteFragment = SignupCompleteFragment.newInstance(email)
                    parentFragmentManager.commit {
                        replace(R.id.frame_layout_authenticate, signupCompleteFragment)
                        addToBackStack(null)
                    }
                } else {
                    Toast.makeText(requireContext(), "Otp không chính xác", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun handleResendOtpClick(email: String?) {
        if (email == null) {
            return
        }
        if (countRunning) {
            stopCountDown()
            userViewModel.registerUser(email) { success, message ->
                Log.i("success1", success.toString())
                if (success) {
                    Toast.makeText(requireContext(), "Resend otp", Toast.LENGTH_SHORT).show()
                    startCountDown(300000)
                }
            }
        }
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
                binding.btnCheckOtp.text = "Gửi lại"
                countRunning = false
            }
        }.start()
        countRunning = true
    }

    private fun stopCountDown() {
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        countRunning = false
    }

    fun additionalMethods() {
        otpTextView.otpListener
        otpTextView.requestFocusOTP()
        otpTextView.otp
        otpTextView.showSuccess()
        otpTextView.showError()
        otpTextView.resetState()
    }

    companion object {
        fun newInstance(email: String): SendOtpFragment {
            val fragment = SendOtpFragment()
            val args = Bundle()
            args.putString("EMAIL", email)
            fragment.arguments = args
            return fragment
        }
    }
}
