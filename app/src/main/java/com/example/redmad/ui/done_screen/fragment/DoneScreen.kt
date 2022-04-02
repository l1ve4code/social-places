package com.example.redmad.ui.done_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.redmad.DoneScreenInterface
import com.example.redmad.data.model.SignModel
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentDoneScreenBinding
import com.example.redmad.ui.MainApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DoneScreen : Fragment() {

    private lateinit var binding: FragmentDoneScreenBinding
    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDoneScreenBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        setButtonActions()

        return binding.root

    }

    private fun setButtonActions() {

        val doneScreenInterface = requireActivity() as DoneScreenInterface

        binding.toList.setOnClickListener {
            lifecycleScope.launch {
                appViewModel.getFeed().observe(viewLifecycleOwner, Observer {
                    if (it.size > 0) {
                        doneScreenInterface.openFeedScreen()
                    }
                    else {
                        doneScreenInterface.openEmptyScreen()
                    }
                })
            }
        }

    }

}