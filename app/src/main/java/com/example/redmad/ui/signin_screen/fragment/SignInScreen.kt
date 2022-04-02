package com.example.redmad.ui.signin_screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.redmad.R
import com.example.redmad.SignInScreenInterface
import com.example.redmad.data.model.SignModel
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentSignInScreenBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.utils.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.RegExp

class SignInScreen : Fragment() {

    private lateinit var binding: FragmentSignInScreenBinding
    private lateinit var appViewModel: AppViewModel
    private lateinit var loadingDialog: LoadingDialog

    private var isFieldsFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignInScreenBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        setButtonActions()

        isFieldsFilled()

        onEnterButtonClick()

        return binding.root

    }

    private fun isFieldsFilled() {
        val emailField = binding.emailField
        val passwordField = binding.passwordField

        binding.emailField.addTextChangedListener {
            binding.emailLoginButton.setTextColor(resources.getColor(R.color.disabled))
            binding.emailLoginButton.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if (emailField.text.toString().isNotEmpty() && passwordField.text.toString()
                    .isNotEmpty()
            ) {
                binding.emailLoginButton.setBackgroundResource(R.drawable.style_login_background)
                binding.emailLoginButton.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

        binding.passwordField.addTextChangedListener {
            binding.emailLoginButton.setTextColor(resources.getColor(R.color.disabled))
            binding.emailLoginButton.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if (emailField.text.toString().isNotEmpty() && passwordField.text.toString()
                    .isNotEmpty()
            ) {
                binding.emailLoginButton.setTextColor(resources.getColor(R.color.white))
                binding.emailLoginButton.setBackgroundResource(R.drawable.style_login_background)
                isFieldsFilled = true
            }
        }

    }

    private fun onEnterButtonClick() {

        val signInScreenInterface = requireActivity() as SignInScreenInterface

        binding.emailLoginButton.setOnClickListener {
            if (isFieldsFilled) {
                lifecycleScope.launch {
                    loadingDialog = activity?.let { it1 -> LoadingDialog(it1) }!!
                    loadingDialog.startLoadingDialog()
                    val _token = runCatching {
                        appViewModel.login(
                            SignModel(
                                binding.emailField.text.toString(),
                                binding.passwordField.text.toString()
                            )
                        )
                    }
                    _token.onSuccess {
                        signInScreenInterface.openDoneScreen()
                        loadingDialog.dismissDialog()
                    }.onFailure {
                        validateEmail(binding.emailField.text.toString())
                        loadingDialog.dismissDialog()
                        val animation = AnimationUtils.loadAnimation(context, R.anim.move)
                        binding.emailLoginButton.startAnimation(animation)
                    }
                }
            }
        }
    }

    private fun validateEmail(email: String){
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if(!email.matches(emailPattern.toRegex())){
            view?.let { Snackbar.make(it, "Неверный E-Mail", Snackbar.LENGTH_SHORT).show() }
        }
        else{
            view?.let { Snackbar.make(it, "Неверный пароль", Snackbar.LENGTH_SHORT).show() }
        }
    }

    private fun setButtonActions() {

        val signInScreenInterface = requireActivity() as SignInScreenInterface

        binding.exitArrow.setOnClickListener {
            signInScreenInterface.closeScreen()
        }

        binding.toSignUp.setOnClickListener {
            signInScreenInterface.openSignUpScreen()
        }

    }

}