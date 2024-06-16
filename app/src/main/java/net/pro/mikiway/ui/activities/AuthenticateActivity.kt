package net.pro.mikiway.ui.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import net.pro.mikiway.R
import net.pro.mikiway.ui.fragments.LoginFragment
import net.pro.mikiway.ui.fragments.SignupFragment

@AndroidEntryPoint
class AuthenticateActivity : AppCompatActivity() {

    private val fragmentManager by lazy { supportFragmentManager }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_authenticate)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_layout_authenticate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isLogin = intent.getBooleanExtra("isLogin", true)
        if (isLogin) {
            replaceFragment(R.id.frame_layout_authenticate, LoginFragment())
        } else {
            replaceFragment(R.id.frame_layout_authenticate, SignupFragment())
        }

    }

    fun replaceFragment(containerId: Int, fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.commit()
    }
}