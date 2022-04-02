package com.example.redmad.ui.start_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.redmad.R
import com.example.redmad.StartScreenInterface
import com.example.redmad.databinding.FragmentStartScreenBinding

class StartScreen : Fragment() {

    private lateinit var binding: FragmentStartScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStartScreenBinding.inflate(inflater, container, false)

        val startScreenInterface = requireActivity() as StartScreenInterface

        binding.emailLoginButton.setOnClickListener {
            startScreenInterface.openSignInScreen()
        }

        binding.toSignUp.setOnClickListener {
            startScreenInterface.openSignUpScreen()
        }

        return binding.root

    }

}