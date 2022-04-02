package com.example.redmad.ui.empty_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redmad.EmptyFriendFragmentInterface
import com.example.redmad.databinding.FragmentFriendEmptyBinding

class FriendEmpty : Fragment() {

    private lateinit var binding: FragmentFriendEmptyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFriendEmptyBinding.inflate(inflater, container, false)

        setButtonActions()

        return binding.root

    }

    private fun setButtonActions() {

        val emptyFriendFragmentInterface = requireActivity() as EmptyFriendFragmentInterface

        binding.findFriends.setOnClickListener {
            emptyFriendFragmentInterface.openSearchScreen()
        }


    }

}