package com.example.redmad.ui.signup_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.redmad.R
import com.example.redmad.SignUpScreenInterface
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentSignUpSecondBinding
import com.example.redmad.ui.MainApplication
import kotlinx.coroutines.launch

class SignUpSecond : Fragment() {

    private lateinit var binding: FragmentSignUpSecondBinding
    private lateinit var appViewModel: AppViewModel

    private var isFieldsFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpSecondBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        isFieldsFilled()

        setButtonActions()

        onEnterButtonClick()

        return binding.root
    }

    private fun isFieldsFilled() {
        val nicknameField = binding.nickField
        val nameField = binding.nameField
        val surnameField = binding.surnameField
        val birthdateField = binding.birthDate

        nicknameField.addTextChangedListener {
            binding.emailLoginButton.setTextColor(resources.getColor(R.color.disabled))
            binding.emailLoginButton.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if (nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ) {
                binding.emailLoginButton.setBackgroundResource(R.drawable.style_login_background)
                binding.emailLoginButton.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

        nameField.addTextChangedListener {
            binding.emailLoginButton.setTextColor(resources.getColor(R.color.disabled))
            binding.emailLoginButton.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if (nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ) {
                binding.emailLoginButton.setBackgroundResource(R.drawable.style_login_background)
                binding.emailLoginButton.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

        surnameField.addTextChangedListener {
            binding.emailLoginButton.setTextColor(resources.getColor(R.color.disabled))
            binding.emailLoginButton.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if (nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ) {
                binding.emailLoginButton.setBackgroundResource(R.drawable.style_login_background)
                binding.emailLoginButton.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

        birthdateField.addTextChangedListener {
            binding.emailLoginButton.setTextColor(resources.getColor(R.color.disabled))
            binding.emailLoginButton.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if (nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ) {
                binding.emailLoginButton.setBackgroundResource(R.drawable.style_login_background)
                binding.emailLoginButton.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

    }

    private fun setButtonActions() {

        val signUpScreenInterface = requireActivity() as SignUpScreenInterface

        binding.exitArrow.setOnClickListener {
            signUpScreenInterface.openSignUpFirstScreen()
        }

    }

    private fun onEnterButtonClick() {

        val signUpScreenInterface = requireActivity() as SignUpScreenInterface

        binding.emailLoginButton.setOnClickListener {
            if (isFieldsFilled) {
                lifecycleScope.launch {

                    val mapData: MutableMap<String, String> = mutableMapOf()

                    mapData.put("first_name", binding.nameField.text.toString())
                    mapData.put("last_name", binding.surnameField.text.toString())
                    mapData.put("nickname", binding.nickField.text.toString())
                    mapData.put("birth_day", binding.birthDate.text.toString())

                    val _edit = runCatching{ appViewModel.editMe(mapData) }

                    _edit.onSuccess {
                        signUpScreenInterface.openDoneScreen()
                    }.onFailure {
                        val animation = AnimationUtils.loadAnimation(context, R.anim.move)
                        binding.emailLoginButton.startAnimation(animation)
                    }
                }
            }
        }
    }

}