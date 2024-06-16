package net.pro.mikiway.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.pro.mikiway.R
import net.pro.mikiway.databinding.FragmentSignupBinding
import net.pro.mikiway.viewModels.UserViewModel

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: FragmentSignupBinding
    private val currentFragmentStack = "Signup"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRedirectLogin.setOnClickListener {
            replaceFragment(R.id.frame_layout_authenticate, LoginFragment(), "Signup")
        }

        binding.btnDangKy.setOnClickListener {
            val email: String = binding.edtEmailSignUp.editText?.text.toString()
            userViewModel.registerUser(email) { success, message ->
                Log.i("success1", success.toString())
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                if (success) {
                    // Navigate to SendOtpFragment
                    val sendOtpFragment = SendOtpFragment.newInstance(email)
                    parentFragmentManager.commit {
                        replace(R.id.frame_layout_authenticate, sendOtpFragment)
                        addToBackStack(null)
                    }
                }
            }
        }

        val dispatcher = requireActivity().onBackPressedDispatcher
        val fragmentManager = requireActivity().supportFragmentManager

        binding.btnBack.setOnClickListener {
            if (fragmentManager.backStackEntryCount > 0) {
                val firstBackStackEntry = fragmentManager.getBackStackEntryAt(0)
                val firstFragmentName = firstBackStackEntry.name

                if (firstFragmentName.equals(currentFragmentStack)) {
                    // If the current fragment is the same as the first fragment in the back stack
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    requireActivity().finish()
                } else {
                    // Otherwise, pop the back stack
                    dispatcher.onBackPressed()
                }
            } else {
                // No more fragments in the back stack, finish the activity
                requireActivity().finish()
            }
        }

    }


    fun replaceFragment(containerId: Int, fragment: Fragment, backStackName: String?) {
        val fragmentManager = requireActivity().supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(containerId)

        if (currentFragment != null && currentFragment.javaClass == fragment.javaClass) {
            // The fragment is already displayed, no need to replace it
            return
        }

        if (backStackName != null) {
            var existingFragment = false
            for (entry in fragmentManager.backStackEntryCount - 1 downTo 0) {
                if (fragmentManager.getBackStackEntryAt(entry).name == backStackName) {
                    existingFragment = true
                    break
                }
            }

            if (!existingFragment) {
                // BackStackName doesn't exist, add fragment to back stack
                fragmentManager.commit {
                    replace(containerId, fragment, backStackName)
                    setReorderingAllowed(true)
                    addToBackStack(backStackName)
                }
            } else {
                // BackStackName exists, no need to add fragment again
                fragmentManager.popBackStack(backStackName, 0)
            }
        } else {
            // Default back stack behavior
            fragmentManager.commit {
                replace(containerId, fragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }


    override fun onResume() {
        super.onResume()

        if (this@SignupFragment.childFragmentManager.popBackStackImmediate()) {
            activity?.finish()
        }
    }

}