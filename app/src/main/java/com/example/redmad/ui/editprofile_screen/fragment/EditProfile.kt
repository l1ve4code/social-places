package com.example.redmad.ui.editprofile_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.redmad.ProfileEditScreenInterface
import com.example.redmad.R
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentEditProfileBinding
import com.example.redmad.ui.MainApplication
import kotlinx.coroutines.launch


class EditProfile : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var appViewModel: AppViewModel

    private var isFieldsFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        onLoad()

        isFieldsFilled()

        setButtonActions()

        onEnterButtonClick()

        return binding.root

    }

    private fun onLoad(){

        lifecycleScope.launch {
            appViewModel.getMe().observe(viewLifecycleOwner, Observer {
                binding.nickField.setText(it.nickname)
                binding.nameField.setText(it.first_name)
                binding.surnameField.setText(it.last_name)
                binding.birthDate.setText(it.birth_day)
            })
        }

    }

    private fun isFieldsFilled(){
        val nicknameField = binding.nickField
        val nameField = binding.nameField
        val surnameField = binding.surnameField
        val birthdateField = binding.birthDate

        nicknameField.addTextChangedListener {
            binding.saveEdits.setTextColor(resources.getColor(R.color.disabled))
            binding.saveEdits.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if(nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ){
                binding.saveEdits.setBackgroundResource(R.drawable.style_login_background)
                binding.saveEdits.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

        nameField.addTextChangedListener {
            binding.saveEdits.setTextColor(resources.getColor(R.color.disabled))
            binding.saveEdits.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if(nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ){
                binding.saveEdits.setBackgroundResource(R.drawable.style_login_background)
                binding.saveEdits.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

        surnameField.addTextChangedListener {
            binding.saveEdits.setTextColor(resources.getColor(R.color.disabled))
            binding.saveEdits.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if(nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ){
                binding.saveEdits.setBackgroundResource(R.drawable.style_login_background)
                binding.saveEdits.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

        birthdateField.addTextChangedListener {
            binding.saveEdits.setTextColor(resources.getColor(R.color.disabled))
            binding.saveEdits.setBackgroundResource(R.drawable.style_disabled_login)
            isFieldsFilled = false
            if(nicknameField.text.toString().isNotEmpty()
                && nameField.text.toString().isNotEmpty()
                && surnameField.text.toString().isNotEmpty()
                && birthdateField.text.toString().isNotEmpty()
            ){
                binding.saveEdits.setBackgroundResource(R.drawable.style_login_background)
                binding.saveEdits.setTextColor(resources.getColor(R.color.white))
                isFieldsFilled = true
            }
        }

    }

    private fun onEnterButtonClick(){

        val profileEditScreenInterface = requireActivity() as ProfileEditScreenInterface

        binding.saveEdits.setOnClickListener {
            if(isFieldsFilled){
                lifecycleScope.launch {

                    val mapData: MutableMap<String, String> = mutableMapOf()

                    mapData.put("first_name", binding.nameField.text.toString())
                    mapData.put("last_name", binding.surnameField.text.toString())
                    mapData.put("nickname", binding.nickField.text.toString())
                    mapData.put("birth_day", binding.birthDate.text.toString())

                    val _edit = runCatching{ appViewModel.editMe(mapData) }

                    _edit.onSuccess {
                        profileEditScreenInterface.closeProfileEditScreen()

                    }.onFailure {
                        val animation = AnimationUtils.loadAnimation(context, R.anim.move)
                        binding.saveEdits.startAnimation(animation)
                    }

                }
            }
        }
    }

    private fun setButtonActions() {

        val profileEditScreenInterface = requireActivity() as ProfileEditScreenInterface

        binding.exitArrow.setOnClickListener {
            profileEditScreenInterface.closeProfileEditScreen()
        }

    }

}