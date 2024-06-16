package net.pro.mikiway.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import net.pro.mikiway.R
import net.pro.mikiway.databinding.ActivitySignupCompleteBinding
import net.pro.mikiway.utils.SharePreferencesManager
import net.pro.mikiway.viewModels.UserViewModel


@AndroidEntryPoint
class SignupCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupCompleteBinding
    lateinit var sharedPreferenceManager: SharePreferencesManager
    private val completeRegistrationViewModel: UserViewModel by viewModels()
     private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_complete)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivitySignupCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("EMAIL").toString()
        Log.i("email", email)
        binding.edtEmailSignUp.editText?.text = Editable.Factory.getInstance().newEditable(email)

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnDangKy.setOnClickListener {
            val username = binding.edtUsernameSignup.editText?.text.toString().trim()
            val password = binding.edtPasswordSignup.editText?.text.toString().trim()
            val repass = binding.edtRepassSignup.editText?.text.toString().trim()

            if (username.equals("") && password.equals("") && repass.equals("")) {
                binding.edtUsernameSignup.editText?.error = "Điền thiếu thông tin"
                binding.edtPasswordSignup.editText?.error = "Điền thiếu thông tin"
                binding.edtRepassSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (username.equals("")) {
                binding.edtUsernameSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (password.equals("")) {
                binding.edtPasswordSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (repass.equals("")) {
                binding.edtRepassSignup.editText?.error = "Điền thiếu thông tin"
                return@setOnClickListener
            }
            if (!password.equals(repass)) {
                binding.edtRepassSignup.editText?.error = "Nhập lại chính xác"
                return@setOnClickListener
            }

            completeRegistrationViewModel.completeRegistration(
                email,
                username,
                password
            ) { success, message, metadata ->
                if (success) {
                    // Chuyển đến màn hình đăng nhập hoặc màn hình chính
                    val intent = Intent(this, LoginActivity::class.java)

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        }


    }
}