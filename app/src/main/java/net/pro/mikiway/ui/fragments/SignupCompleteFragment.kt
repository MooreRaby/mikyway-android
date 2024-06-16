package net.pro.mikiway.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.pro.mikiway.R
import net.pro.mikiway.databinding.ActivitySignupCompleteBinding
import net.pro.mikiway.databinding.FragmentSignupCompleteBinding
import net.pro.mikiway.ui.activities.LoginActivity
import net.pro.mikiway.utils.SharePreferencesManager
import net.pro.mikiway.viewModels.UserViewModel

@AndroidEntryPoint
class SignupCompleteFragment : Fragment() {
    private lateinit var binding: FragmentSignupCompleteBinding
    lateinit var sharedPreferenceManager: SharePreferencesManager
    private val completeRegistrationViewModel: UserViewModel by viewModels()
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("EMAIL")
        if (email != null) {
            Log.i("email", email)
        }
        binding.edtEmailSignUp.editText?.text = Editable.Factory.getInstance().newEditable(email)

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnDangKy.setOnClickListener {
            val username = binding.edtUsernameSignup.editText?.text.toString().trim()
            val password = binding.edtPasswordSignup.editText?.text.toString().trim()
            val repass = binding.edtRepassSignup.editText?.text.toString().trim()

            if (username == "" && password == "" && repass == "") {
                binding.edtUsernameSignup.editText?.error = "Điền thiếu thông tin"
                binding.edtPasswordSignup.editText?.error = "Điền thiếu thông tin"
                binding.edtRepassSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (username == ("")) {
                binding.edtUsernameSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (password == ("")) {
                binding.edtPasswordSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (repass == ("")) {
                binding.edtRepassSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (password != (repass)) {
                binding.edtRepassSignup.editText?.error = "Nhập lại chính xác"
                return@setOnClickListener
            }

            if (email != null) {
                completeRegistrationViewModel.completeRegistration(
                    email,
                    username,
                    password
                ) { success, message, metadata ->
                    if (success) {

                    }
                }
            }
        }
    }


    companion object {
        fun newInstance(email: String): SignupCompleteFragment {
            val fragment = SignupCompleteFragment()
            val args = Bundle()
            args.putString("EMAIL", email)
            fragment.arguments = args
            return fragment
        }
    }
}