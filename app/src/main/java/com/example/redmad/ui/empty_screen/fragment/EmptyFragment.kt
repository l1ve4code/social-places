package com.example.redmad.ui.empty_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redmad.EmptyFragmentInterface
import com.example.redmad.databinding.FragmentEmptyBinding

class EmptyFragment : Fragment() {

    private lateinit var binding: FragmentEmptyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEmptyBinding.inflate(inflater, container, false)

        setButtonActions()

        return binding.root

    }

    private fun setButtonActions() {

        val emptyFragmentInterface = requireActivity() as EmptyFragmentInterface

        binding.findFriendButton.setOnClickListener {
            emptyFragmentInterface.openSearchScreen()
        }

    }

}