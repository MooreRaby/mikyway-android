package net.pro.mikiway.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import net.pro.mikiway.R
import net.pro.mikiway.databinding.ActivitySignupBinding
import net.pro.mikiway.viewModels.UserViewModel

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDangKy.setOnClickListener {
            val email: String = binding.edtEmailSignUp.editText?.text.toString()
            userViewModel.registerUser(email) { success, message ->
                Log.i("success1",success.toString())
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    // Chuyển đến màn hình nhập OTP
                    val intent = Intent(this, SendOtpActivity::class.java)
                    intent.putExtra("EMAIL", email)
                    startActivity(intent)
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

}