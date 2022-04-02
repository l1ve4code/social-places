package com.example.redmad.ui.empty_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redmad.EmptyPostFragmentInterface
import com.example.redmad.databinding.FragmentPostsEmptyBinding

class PostsEmpty : Fragment() {

    private lateinit var binding: FragmentPostsEmptyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostsEmptyBinding.inflate(inflater, container, false)

        setButtonActions()

        return binding.root

    }

    private fun setButtonActions() {

        val emptyPostFragmentInterface = requireActivity() as EmptyPostFragmentInterface

        binding.createPost.setOnClickListener {
            emptyPostFragmentInterface.openAddScreen()
        }

    }

}