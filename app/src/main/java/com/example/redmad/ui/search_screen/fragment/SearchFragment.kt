package com.example.redmad.ui.search_screen.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redmad.SearchScreenInterface
import com.example.redmad.data.model.FriendModel
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentSearchBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.ui.search_screen.adapter.SearchAdapter
import kotlinx.coroutines.launch

interface AdapterInterface {
    fun addFriend(id: String)
}

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var appViewModel: AppViewModel

    private lateinit var searchAdapter: SearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)

        searchAdapter = SearchAdapter(
            object : AdapterInterface{
                override fun addFriend(id: String) {
                    lifecycleScope.launch {
                        appViewModel.addFriend(FriendModel(id))
                    }
                }
            }
        )

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        binding.recyclerSearch.layoutManager = layoutManager
        binding.recyclerSearch.adapter = searchAdapter

        setButtonActions()

        onLoad()

        writeAction()

        return binding.root

    }

    private fun writeAction() {
        binding.nameField.addTextChangedListener {
            lifecycleScope.launch {
                if (binding.nameField.text.toString() == "") {
                    binding.recyclerSearch.visibility = View.GONE
                    binding.subtitleText.visibility = View.VISIBLE
                } else {
                    binding.recyclerSearch.visibility = View.VISIBLE
                    binding.subtitleText.visibility = View.GONE
                }
                appViewModel.search(binding.nameField.text.toString())
                    .observe(viewLifecycleOwner, Observer {
                        searchAdapter.userList = it
                    })
                searchAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun onLoad() {
        binding.recyclerSearch.visibility = View.GONE
    }

    private fun setButtonActions() {

        val searchScreenInterface = requireActivity() as SearchScreenInterface

        binding.exitArrow.setOnClickListener {
            searchScreenInterface.closeSearchScreen()
        }

    }



}