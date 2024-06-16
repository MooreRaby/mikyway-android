package net.pro.mikiway.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import net.pro.mikiway.R
import net.pro.mikiway.databinding.FragmentPersonBinding
import net.pro.mikiway.ui.activities.AuthenticateActivity


class PersonFragment : Fragment() {
    private lateinit var binding: FragmentPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = (context as AppCompatActivity).window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.orange)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnDangNhap.setOnClickListener {
            val intent = Intent(requireContext(),AuthenticateActivity::class.java)
            intent.putExtra("isLogin",true)
            startActivity(intent)
        }

        binding.btnDangKy.setOnClickListener {
            val intent = Intent(requireContext(),AuthenticateActivity::class.java)
            intent.putExtra("isLogin",false)
            startActivity(intent)
        }
    }
}