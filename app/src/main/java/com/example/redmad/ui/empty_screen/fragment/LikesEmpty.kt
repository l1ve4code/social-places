package com.example.redmad.ui.empty_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.redmad.EmptyLikeFragmentInterface
import com.example.redmad.databinding.FragmentLikesEmptyBinding


class LikesEmpty : Fragment() {

    private lateinit var binding: FragmentLikesEmptyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLikesEmptyBinding.inflate(inflater, container, false)

        setButtonActions()

        return binding.root

    }

    private fun setButtonActions() {

        val emptyLikeFragmentInterface = requireActivity() as EmptyLikeFragmentInterface

        binding.goToFeed.setOnClickListener {
            emptyLikeFragmentInterface.closeEmptyLikeScreen()
        }

    }

}